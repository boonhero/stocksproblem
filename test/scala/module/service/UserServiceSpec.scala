package scala.module.service

import model.{Currency, Stock, User}
import module.data.{StockDao, StockTransactionDao, UserDao}
import module.data.mock.{MockStockDao, MockStockTransactionDao}
import module.service.{StockTransactionService, UserService}
import org.joda.time.DateTime
import org.specs2.matcher.Matchers
import org.specs2.mock.Mockito
import org.specs2.mutable.{BeforeAfter, Specification}

class UserServiceSpec extends Specification with Matchers with Mockito {

  val userDao = mock[UserDao]
  val stockDao = mock[StockDao]
  val stockTransactionService = mock[StockTransactionService]
  var userService = new UserService(userDao, stockTransactionService, stockDao)


  "user service selling" should {
     "sell same stock will get 0 profit/loss" in {
       val user = Some(User(name = "testId", stocks = List(
         Stock(
           userStockId = "ABC-1",
           _id = "ABC-a",
           name = "ABC",
           tradeDate = new DateTime(),
           quantity = 10,
           price = BigDecimal(10),
           Currency("USD", 1),
           profitLoss = BigDecimal(0)
         ))))

       val stock = Stock(
         userStockId = "",
         _id = "ABC-a",
         name = "ABC",
         tradeDate = new DateTime(),
         quantity = -1,
         price = BigDecimal(10),
         Currency("USD", 1),
         profitLoss = BigDecimal(0)
       )

       userDao.find(anyString) returns user
       stockDao.findBy(anyString, any[DateTime]) returns Some(stock)

       userService.sell(userStockId = "ABC-1",userId = "testId", tradeDate = any[DateTime], quantity = 10)

       user.get.stocks(0).quantity mustEqual 0
       user.get.stocks(0).profitLoss mustEqual 0
     }

    "sell stock with 0 quantity will return none" in {
      val user = Some(User(name = "testId", stocks = List(
        Stock(
          userStockId = "ABC-1",
          _id = "ABC-a",
          name = "ABC",
          tradeDate = new DateTime(),
          quantity = 0,
          price = BigDecimal(10),
          Currency("USD", 1),
          profitLoss = BigDecimal(0)
        ))))

      userDao.find(anyString) returns user

      userService.sell(userStockId = "ABC-1",userId = "testId", tradeDate = any[DateTime], quantity = 10) === None
    }

    "sell stock with invalid userStockId will return none" in {
      val user = Some(User(name = "testId", stocks = List(
        Stock(
          userStockId = "ABC-1",
          _id = "ABC-a",
          name = "ABC",
          tradeDate = new DateTime(),
          quantity = 0,
          price = BigDecimal(10),
          Currency("USD", 1),
          profitLoss = BigDecimal(0)
        ))))

      userDao.find(anyString) returns user

      userService.sell(userStockId = "ABC-1",userId = "INVALID_USERSTOCKID", tradeDate = any[DateTime], quantity = 10) === None
    }
  }

  "user service buying" should {
    "buy a stock" in {
      val user = Some(User(name = "testId", stocks = List()))

      val stock = Stock(
        userStockId = "",
        _id = "ABC-a",
        name = "ABC",
        tradeDate = new DateTime(),
        quantity = -1,
        price = BigDecimal(10),
        Currency("USD", 1),
        profitLoss = BigDecimal(0)
      )

      stockDao.findBy(anyString) returns Some(stock)
      userDao.find(anyString) returns user

      userService.buy(stockId = "ABC-a", userId = "testId", quantity = 10)

      user.get.stocks.size === 1
      user.get.stocks(0).quantity === 10
    }

    "buy a invalid stock" in {
      val user = Some(User(name = "testId", stocks = List()))

      stockDao.findBy(anyString) returns None

      userService.buy(stockId = "ABC-a", userId = "testId", quantity = 10) === None

      user.get.stocks.size === 0
    }
  }

}
