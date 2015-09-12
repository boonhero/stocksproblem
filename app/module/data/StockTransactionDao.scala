package module.data

import com.google.inject.ImplementedBy
import model.{ComputeResult, StockTransaction}
import module.MongoCli
import module.data.mock.MockStockTransactionDao
import scala.Some
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb.casbah.WriteConcern
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import org.bson.types.ObjectId
import com.mongodb.CommandResult

@ImplementedBy(classOf[StockTransactionDaoImpl])
trait StockTransactionDao {
  def findBy(userStockId: String): List[StockTransaction]

  def removeAll(): Unit

  def findAll(): List[StockTransaction]

  def save(transaction: StockTransaction): Unit

}
class StockTransactionDaoImpl extends StockTransactionDao {

  override def save(transaction: StockTransaction): Unit = {
    MongoCli.stockTransactionColl.save(transaction.asDbObject())
  }

  override def findAll(): List[StockTransaction] = {
    val results = MongoCli.stockTransactionColl.find(MongoDBObject.empty)
    val stockTransactions = for (item <- results) yield StockTransaction.asObject(item)
    stockTransactions.toList
  }

  override def removeAll(): Unit = {
    MongoCli.stockTransactionColl.remove(MongoDBObject.empty)
  }

  override def findBy(userStockId: String): List[StockTransaction] = {
    val results = MongoCli.stockTransactionColl.find(MongoDBObject("stock.userStockId" -> userStockId))
    val stockTransactions = for (item <- results) yield StockTransaction.asObject(item)
    stockTransactions.toList
  }
}
