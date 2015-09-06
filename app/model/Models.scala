package model

import com.github.nscala_time.time.Imports._

case class Currency (name: String, rate: Double)
case class Stock(userStockId: String, _id: String, name: String, tradeDate: DateTime, quantity: Int, price: BigDecimal, currency: Currency, profitLoss: BigDecimal)
case class User(name: String, var stocks: List[Stock]) {
  def removeAllStocks(): Unit = {
    stocks = List()
  }


  def getStockByUserStockId(id: String) : Option[Stock] = {
    stocks.filter(p => p.userStockId.equals(id)) match {
      case Nil => None
      case stock :: Nil => Some(stock)
      case stocks => Some(stocks(0))
    }
  }


  def getStock(stockId: String): Option[Stock] = {
    stocks.filter(p => p._id.equals(stockId)) match {
      case Nil => None
      case stock :: Nil => Some(stock)
      case stocks => Some(stocks(0))
    }
  }

  def removeStock(userStockId: String): Unit = {
    this.stocks = this.stocks.filter(p => p.userStockId != userStockId)
  }

  def addStock(stock: Stock): Unit = {
    this.stocks ::= stock
  }
}

case class ComputeResult(name: String, totalBalance: BigDecimal)

case class StockTransaction(_id: String, stock: Stock, usdPrice: BigDecimal, transactionType: String, createdDate: DateTime, createdBy: String)