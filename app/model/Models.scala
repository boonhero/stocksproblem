package model

import java.util.Date

import com.github.nscala_time.time.Imports._
import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

case class Currency (name: String, rate: Double) {
  def asDbObject(): DBObject = {
    val b = MongoDBObject.newBuilder
    b += "name" -> name
    b += "rate" -> rate

    b.result()
  }
}

object Currency {
  def asObject(item: MongoDBObject) = Currency(
    item.getAs[String]("name").get,
    item.getAs[Double]("rate").get
  )
}

case class User(name: String, var stocks: List[Stock]) {
  def removeAllStocks(): Unit = {
    stocks = List()
  }


  def getStockByUserStockId(id: String) = stocks.filter(p => p.userStockId.equals(id)) match {
      case Nil => None
      case stock :: Nil => Some(stock)
      case stocks => Some(stocks(0))
  }


  def getStock(stockId: String) = stocks.filter(p => p._id.equals(stockId)) match {
      case Nil => None
      case stock :: Nil => Some(stock)
      case stocks => Some(stocks(0))
  }

  def removeStock(userStockId: String): Unit = {
    this.stocks = this.stocks.filter(p => p.userStockId != userStockId)
  }

  def addStock(stock: Stock): Unit = {
    this.stocks ::= stock
  }
}

case class ComputeResult(name: String, totalBalance: BigDecimal)

case class Stock(userStockId: String, _id: Option[String], name: String, tradeDate: Date, quantity: Int, price: BigDecimal, currency: Currency, profitLoss: BigDecimal) {
  def asDbObject(): DBObject = {
    val b = MongoDBObject.newBuilder
    _id match {
      case Some(objectId) => b += "_id" -> objectId
      case None => {}
    }
    b += "userStockId" -> userStockId
    b += "price" -> price.toString()
    b += "tradeDate" -> tradeDate
    b += "name" -> name
    b += "quantity" -> quantity
    b += "currency" -> currency.asDbObject()
    b += "profitLoss" -> profitLoss.toString()

    b.result()
  }
}

object Stock {
  def asObject(item: MongoDBObject) = Stock(
    item.getAs[String]("userStockId").get,
    Some(item.getAs[String]("_id").get),
    item.getAs[String]("name").get,
    item.getAs[Date]("tradeDate").get,
    item.getAs[Int]("quantity").get,
    BigDecimal(item.getAs[String]("price").get),
    Currency.asObject(new MongoDBObject(item.as[Currency]("currency").asInstanceOf[DBObject])),
    BigDecimal(item.getAs[String]("profitLoss").get)
  )
}

case class StockTransaction(_id: Option[String], stock: Stock, usdPrice: BigDecimal, transactionType: String, createdDate: DateTime, createdBy: String) {
  def asDbObject(): DBObject = {
    val b = MongoDBObject.newBuilder
    _id match {
      case Some(objectId) => b += "_id" -> new ObjectId(objectId)
      case None => {}
    }
    b += "stock" -> stock.asDbObject()
    b += "usdPrice" -> usdPrice.toString()
    b += "transactionType" -> transactionType
    b += "createdDate" -> createdDate.toDate
    b += "createdBy" -> createdBy

    b.result()
  }
}

object StockTransaction {
  def asObject(item: MongoDBObject) = StockTransaction(
    Some(item.getAs[ObjectId]("_id").get.toString),
    Stock.asObject(new MongoDBObject(item.as[Stock]("stock").asInstanceOf[DBObject])),
    BigDecimal(item.getAs[String]("usdPrice").get),
    item.getAs[String]("transactionType").get,
    new DateTime(item.getAs[Date]("createdDate").get),
    item.getAs[String]("createdBy").get
  )
}