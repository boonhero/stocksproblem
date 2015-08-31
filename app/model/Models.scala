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
  val messageColl = db("messages")
  val stockColl = db("stocks")
//  val userColl = db("users")
  val stockTransactionColl = db("stocktransactions")
  println("MongoCli init")
}

trait MongoDbModel {
   def asMongoDbObject() : MongoDBObject
}

case class Stock (_id: String, name: String)  extends MongoDbModel {
  override def asMongoDbObject(): MongoDBObject = {
    MongoDBObject("name" -> name)
  }
}

case class StockTransaction(_id: String, var stock: Stock, price: Int, dateOrdered: java.util.Date, transactionType: String, transactionDate: java.util.Date, userId: String)  extends MongoDbModel {
  override def asMongoDbObject() : MongoDBObject = {
    MongoDBObject("stock" -> stock,
      "price" -> price,
      "dateOrdered" -> dateOrdered,
      "transactionType" -> transactionType,
      "transactionDate" -> transactionDate,
      "userId" -> userId
    )
  }
}
//case class User(_id: String, username: String, stocks: List[Stock])  extends MongoDbModel {
//  override def asMongoDbObject(): MongoDBObject = {
//    MongoDBObject("username" -> username, "stocks" -> stocks)
//  }
//}

case class Message(_id: String, message: String) extends MongoDbModel {
  override def asMongoDbObject(): MongoDBObject = {
    MongoDBObject("message" -> message)
  }
}

object Message {
  val logger: Logger = Logger(this.getClass())

  def all(): List[Message] = {
    MongoCli.messageColl.find(MongoDBObject.empty).toList.map(f =>
      Message(f.getAs[ObjectId]("_id").getOrElse(new ObjectId()).toString, f.getAs[String]("message").get))
  }

  def create(message: Message): ObjectId = {
    val doc = new MongoDBObject(message.asMongoDbObject())
    MongoCli.messageColl.insert(doc)
    doc.as[ObjectId]("_id")
  }

}

object Stock {
  val logger: Logger = Logger(this.getClass())

  def all(): List[Stock] = {
    MongoCli.stockColl.find(MongoDBObject.empty).toList.map(f => mongoDbToObject(f))
  }

  def findByName(name: String): Option[Stock] = {
    MongoCli.stockColl.find(MongoDBObject("name" -> name)).toList match {
      case Nil => None
      case mongoObject :: Nil => Some(mongoDbToObject(mongoObject))
    }
  }

  def create(stock: Stock): ObjectId = {
    val doc = new MongoDBObject(stock.asMongoDbObject())
    MongoCli.stockColl.insert(doc)
    doc.as[ObjectId]("_id")
  }

  def mongoDbToObject(f: MongoDBObject): Stock = {
    Stock(f.getAs[ObjectId]("_id").getOrElse(new ObjectId()).toString, f.getAs[String]("name").get)
  }
}

object StockTransaction {
  val logger: Logger = Logger(this.getClass())

  def all(): List[StockTransaction] = {
    MongoCli.stockTransactionColl.find(MongoDBObject.empty).toList.map(f => mongoDbToObject(f))
  }

  def findUser(userId: String): List[StockTransaction] = {
    MongoCli.stockTransactionColl.find(MongoDBObject("userId" -> userId)).toList match {
      case Nil => List()
      case mongoObjects => mongoObjects map {each => mongoDbToObject(each)}
    }
  }

  def create(stockTransaction: StockTransaction): ObjectId = {
    val doc = new MongoDBObject(stockTransaction.asMongoDbObject())
    MongoCli.stockTransactionColl.insert(doc)
    doc.as[ObjectId]("_id")
  }

  def mongoDbToObject(f: MongoDBObject): StockTransaction = ???
}