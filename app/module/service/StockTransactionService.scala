package module.service

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import model.{ComputeResult, Stock, StockTransaction}
import module.data.StockTransactionDao
import org.joda.time.DateTime
import utility.CurrencyHelper

@Singleton
class StockTransactionService @Inject() (stockTransactionDao: StockTransactionDao) {

  def transact(stock: Stock, transactionType: String, createdBy: String): String = {
    val transactionId: String = UUID.randomUUID().toString
    stockTransactionDao.save(StockTransaction(
      UUID.randomUUID().toString,
      stock,
      CurrencyHelper.base(stock.price, stock.currency.rate),
      transactionType,
      new DateTime(),
      createdBy
    ))
    transactionId
  }

  def findAll(): Option[List[StockTransaction]] = {
    stockTransactionDao.findAll()
  }

  /**
   * Converts the value to negative if type "SELL"
   * @param transactionType
   * @param value
   * @return
   */
  def convert(transactionType: String, value: BigDecimal) = {
    transactionType match {
      case "BUY" => -1 * value
      case "SELL" => value
    }
  }

  /**
   * Computes profit or loss for each Stock name
   * @return
   */
  def getProfitOrLossForAllStocks(): List[ComputeResult] = {
    findAll() match {
      case Some(all) => {
        all.map(p => (p.stock.name, convert(p.transactionType, (p.usdPrice * p.stock.quantity * p.stock.currency.rate))))
          .groupBy(_._1).map(kv => ComputeResult(kv._1, kv._2.map(_._2).sum)).toList
      }
      case None => List()
    }
  }

  def getProfitOrLossFor(userStockId: String): BigDecimal = {
      stockTransactionDao.findBy(userStockId).map(p => convert(p.transactionType, (p.usdPrice * p.stock.quantity * p.stock.currency.rate))).sum
  }
}
