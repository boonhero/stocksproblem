package module

import com.mongodb.casbah.MongoClient
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