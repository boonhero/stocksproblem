package module.data.mock

import com.google.inject.Singleton
import model.{StockTransaction, Currency, Stock}
import module.data.StockDao
import org.joda.time.DateTime
import utility.DateHelper

/**
 * Created by asales on 1/9/2015.
 */
@Singleton
class MockStockDao extends StockDao {
  var stocks = List[Stock] (
    Stock("AB01", "ABC", DateHelper.formatter.parseDateTime("03/11/2015"), -1, 10, Currency("USD", 1.0))
  )

  override def findAll(): List[Stock] = {
    stocks
  }

  override def findBy(name: String, tradeDate: DateTime): Option[Stock] = {
    stocks.filter(p => p.name.equals(name)).filter(p => p.tradeDate.withTimeAtStartOfDay().isEqual(tradeDate.withTimeAtStartOfDay())) match {
      case Nil => None
      case stock :: Nil => Some(stock)
      case stocks => Some(stocks(0))
    }
  }

  override def findBy(tradeDate: DateTime): List[Stock] = {
    stocks.filter(p => p.tradeDate.withTimeAtStartOfDay().isEqual(tradeDate.withTimeAtStartOfDay())) match {
      case Nil => List()
      case stocks => stocks
    }
  }

  override def replaceStocks(stocks: List[Stock]): Unit = {
    this.stocks = stocks
  }

  override def findBy(id: String): Option[Stock] = {
    stocks.filter(p => p._id.equals(id)) match {
      case Nil => None
      case stock :: Nil => Some(stock)
      case stocks => Some(stocks(0))
    }
  }


}
