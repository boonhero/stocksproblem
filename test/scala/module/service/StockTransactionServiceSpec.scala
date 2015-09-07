package scala.module.service

import model.{StockTransaction, Stock, Currency, ComputeResult}
import module.data.StockTransactionDao
import module.data.mock.{MockStockProvider, MockStockDao}
import module.service.StockTransactionService
import org.joda.time.DateTime
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class StockTransactionServiceSpec extends Specification with Mockito {

  "stock provider " should {
    "replace mock stockdao data" in {
      val mockStockDao = new MockStockDao
      val mockDataService = new MockStockProvider(mockStockDao)
      mockDataService.readStocks()
      mockStockDao.stocks must haveSize(93)
    }
  }

  "StockTransactionService get profit or loss" should {
    "get profit or loss by accumulated profit and losses" in {
      val stockTransactionDao = mock[StockTransactionDao]
      val stockTransactionService: StockTransactionService = new StockTransactionService(stockTransactionDao)

      stockTransactionDao.findAll() returns Some(List[StockTransaction](
        StockTransaction(_id = "T-XYZ1", Stock(userStockId = "XYZ-1",_id = "XYZ-a", name = "XYZ", tradeDate = new DateTime(), quantity = 10, price = BigDecimal("100"), Currency("USD", 1.0), BigDecimal(10)),usdPrice = 1000, "SELL", new DateTime(), "testId"),
        StockTransaction(_id = "T-XYZ1", Stock(userStockId = "XYZ-1",_id = "XYZ-a", name = "XYZ", tradeDate = new DateTime(), quantity = 10, price = BigDecimal("100"), Currency("USD", 1.0), BigDecimal(10)),usdPrice = 1000, "SELL", new DateTime(), "testId")
      ))


      val result: List[ComputeResult] = stockTransactionService.getProfitOrLossForAllStocks()
      result.size === 1
      result(0).name === "XYZ"
      result(0).totalBalance === 20
    }
  }
}
