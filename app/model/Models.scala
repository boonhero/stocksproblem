package model

import java.util.Date

import com.mongodb.casbah.MongoClient
import play.api.Logger

object MongoManager {
  val mongoConnection = MongoClient("128.199.158.79", 27017)
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

case class Message(var id: String, message: String)


/**
 * Helper for pagination
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

object Message {
  val logger: Logger = Logger(this.getClass())

  def insert(message: Message) = Option[String] {
    val uuid: String = java.util.UUID.randomUUID.toString
    message.id = uuid
    Messages.messages :+ message
    logger.info(s"Messages.messages.size: ${Messages.messages.size}")
    uuid
  }

  def findAll(): List[Message] = Messages.messages
}

object StockManager {
  def appendStock(stock: Stock, userId: String) : Unit = {
    Stocks.names + stock.name
    StockTransactions.stockTransactions :+ StockTransaction(stock, new Date(), userId)
  }
}
