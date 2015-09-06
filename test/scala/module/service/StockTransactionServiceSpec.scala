package scala.module.service

import module.data.mock.{MockStockProvider, MockStockDao}
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

//  "StockTransactionService" should {
//    "get no profit and no loss" in {
//      module.stockTransactionDao.stockTransactions = List[StockTransaction](
//        StockTransaction("T-ABC1", Stock("ABC1", "ABC", new DateTime(), 10, BigDecimal("100"), Currency("USD", 1.0)), "BUY", new DateTime(), "testId"),
//        StockTransaction("T-ABC1", Stock("ABC1", "ABC", new DateTime(), 10, BigDecimal("100"), Currency("USD", 1.0)), "BUY", new DateTime(), "testId"),
//        StockTransaction("T-ABC1", Stock("ABC1", "ABC", new DateTime(), 10, BigDecimal("100"), Currency("USD", 1.0)), "SELL", new DateTime(), "testId"),
//        StockTransaction("T-ABC1", Stock("ABC1", "ABC", new DateTime(), 10, BigDecimal("100"), Currency("USD", 1.0)), "SELL", new DateTime(), "testId")
//      )
//      val result = module.stockTransactionService.getProfitOrLossForAllStocks()
//
//      println(result.get)
//    }
//  }
//
//
//
//
//  it should "get multiple profit" in {
//    module.stockTransactionDao.stockTransactions = List[StockTransaction](
//      StockTransaction("T-ABC1", Stock("ABC1", "ABC", new DateTime(), 10, BigDecimal("100"), Currency("USD", 1.0)), "BUY", new DateTime(), "testId"),
//      StockTransaction("T-ABC1", Stock("ABC1", "ABC", new DateTime(), 10, BigDecimal("100"), Currency("USD", 1.0)), "BUY", new DateTime(), "testId"),
//      StockTransaction("T-XYZ1", Stock("XYZ1", "XYZ", new DateTime(), 10, BigDecimal("100"), Currency("USD", 1.0)), "BUY", new DateTime(), "testId"),
//      StockTransaction("T-XYZ1", Stock("XYZ1", "XYZ", new DateTime(), 10, BigDecimal("100"), Currency("USD", 1.0)), "BUY", new DateTime(), "testId")
//    )
//    val result = module.stockTransactionService.getProfitOrLossForAllStocks()
//
//    println(result.get)
//  }

}
