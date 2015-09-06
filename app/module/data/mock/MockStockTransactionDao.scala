package module.data.mock


import com.google.inject.Singleton
import model.{StockTransaction, Currency, Stock}
import module.data.StockTransactionDao
import org.joda.time.DateTime

/**
 * Created by asales on 1/9/2015.
 */
@Singleton
class MockStockTransactionDao extends StockTransactionDao {
  var stockTransactions = List[StockTransaction]()

  override def save(transaction: StockTransaction): Unit = {
    stockTransactions ::= transaction
  }

  override def findAll(): Option[List[StockTransaction]] = {
    Some(stockTransactions)
  }

  override def removeAll(): Unit = {
    this.stockTransactions = List()
  }

  override def findBy(userStockId: String): List[StockTransaction] = {
    stockTransactions.filter(f => f.stock.userStockId.equals(userStockId))
  }
}
