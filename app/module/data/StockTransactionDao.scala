package module.data

import com.google.inject.ImplementedBy
import model.StockTransaction
import module.data.mock.MockStockTransactionDao

@ImplementedBy(classOf[MockStockTransactionDao])
trait StockTransactionDao {
  def findAll(): Option[List[StockTransaction]] 

  def save(transaction: StockTransaction): Unit

}
class StockTransactionDaoImpl extends StockTransactionDao {
  override def save(transaction: StockTransaction): Unit = ???

  override def findAll(): Option[List[StockTransaction]] = ???
}
