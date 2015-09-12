package module.data

import java.util.Date

import com.google.inject.ImplementedBy
import com.mongodb.casbah.Imports._
import model.{StockTransaction, Stock}
import module.MongoCli
import module.data.mock.MockStockDao
import org.joda.time.DateTime

@ImplementedBy(classOf[MockStockDao])
trait StockDao {
  def replaceStocks(stocks: List[Stock]): Unit
  def findAll(): List[Stock]
  def findBy(name: String, tradeDate: DateTime): Option[Stock]
  def findBy(tradeDate: Date): List[Stock]
  def findBy(id: String): Option[Stock]
  def save(stock: Stock): Unit
}


class StockDaoImpl extends StockDao {
  override def findBy(name: String, tradeDate: DateTime): Option[Stock] = ???

  override def findBy(tradeDate: Date): List[Stock] = ???

  override def findAll(): List[Stock] = {
    val results = MongoCli.stockColl.find(MongoDBObject.empty)
    val stocks = for (item <- results) yield Stock.asObject(item)
    stocks.toList
  }

  override def replaceStocks(stocks: List[Stock]): Unit = ???

  override def findBy(id: String): Option[Stock] = ???

  override def save(stock: Stock): Unit = {
    MongoCli.stockColl.save(stock.asDbObject())
  }
}
