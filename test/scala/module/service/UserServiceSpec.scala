package scala.module.service

import module.data.mock.{MockStockDao, MockStockTransactionDao, MockUserDao}
import module.service.ServiceModule
import org.specs2.matcher.Matchers
import org.specs2.mutable.Specification

class UserServiceSpec extends Specification with Matchers {

  val module = new ServiceModule {
    override lazy val userDao = new MockUserDao
    override lazy val stockTransactionDao = new MockStockTransactionDao
    override lazy val stockDao = new MockStockDao
  }

//  behavior of "UserService"
//
//  it should "get user" in {
//    assert(module.userService.getUser("testId") match {
//      case Some(user) => user.name.equals("testId")
//      case None => false
//    })
//  }
//
//  it should "buy stocks" in {
//    val stockABC10 = Stock("ABC01", "ABC", new DateTime(), 10, BigDecimal("10"), Currency("USD", 1.0))
//    val userId = "testId"
//
//    assert(module.userService.buy(stockABC10, userId) match {
//      case Some(transactionId) => {
//        assert(module.userService.getUser("testId") match {
//          case Some(user) => user.stocks.nonEmpty
//          case None => false
//        })
//
//        assert(module.stockTransactionService.findAll() match {
//          case Some(Nil) => false
//          case None => false
//          case _ => true
//        })
//
//        true
//      }
//      case None => false
//    })
//
//  }
//
//  it should "sell bought stocks" in {
//    val stockABC10 = Stock("ABC01", "ABC", new DateTime(), 10, BigDecimal("10"), Currency("USD", 1.0))
//    assert(module.userService.sell(stockABC10, "testId") match {
//      case Some(transactionId) => {
//        assert(module.userService.getUser("testId") match {
//          case Some(user) => !user.stocks.nonEmpty
//          case None => false
//        })
//
//        true
//      }
//      case None => false
//    })
//  }

}
