package module.data

import com.google.inject.ImplementedBy
import model.Stock
import module.data.mock.MockStockDao
import org.joda.time.DateTime

@ImplementedBy(classOf[MockStockDao])
trait StockDao {
  def replaceStocks(stocks: List[Stock]): Unit
  def findAll(): List[Stock]
  def findBy(name: String, tradeDate: DateTime): Option[Stock]
  def findBy(tradeDate: DateTime): List[Stock]
  def findBy(id: String): Option[Stock]
}


class StockDaoImpl extends StockDao {
  override def findBy(name: String, tradeDate: DateTime): Option[Stock] = ???

  override def findBy(tradeDate: DateTime): List[Stock] = ???

  override def findAll(): List[Stock] = ???

  override def replaceStocks(stocks: List[Stock]): Unit = ???

  override def findBy(id: String): Option[Stock] = ???
}
