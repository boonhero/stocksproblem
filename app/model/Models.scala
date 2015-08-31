package model

import java.util.Date

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.{Imports, MongoDBObject}
import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.Logger

object MongoCli {
  val logger: Logger = Logger(this.getClass())

  val mongoClient = MongoClient("localhost", 27017)
  val db = mongoClient("stockproblems")
  val messageColl = db("message")
  println("MongoCli init")
}

object Messages {
  val messages: List[Message] = List()
}

object Stocks {
  val names: Set[String] = Set()
}

object StockTransactions {
  val stockTransactions: List[StockTransaction] = List()
}

case class Stock (name: String, price: Int, dateOrdered: java.util.Date)
case class StockTransaction(stock: Stock, transactionDate: java.util.Date, userId: String)
case class Message(_id: String, message: String) {
  def asMongoDbObject(): MongoDBObject = {
    MongoDBObject("message" -> message)
  }
}

/**
 * Helper for pagination
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

object Message {
  val logger: Logger = Logger(this.getClass())

  def all(): List[Message] = {
    MongoCli.messageColl.find(MongoDBObject.empty).toList.map(f =>
      Message(f.getAs[ObjectId]("_id").getOrElse(new ObjectId()).toString, f.getAs[String]("message").get))
  }

  def create(message: Message): ObjectId = {
    var doc = new MongoDBObject(message.asMongoDbObject())
    MongoCli.messageColl.insert(doc)
    doc.as[ObjectId]("_id")
  }

}

object StockManager {
  def appendStock(stock: Stock, userId: String) : Unit = {
    Stocks.names + stock.name
    StockTransactions.stockTransactions :+ StockTransaction(stock, new Date(), userId)
  }
}
