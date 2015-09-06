package module.service

import java.util.UUID

import com.google.inject.{Inject, Singleton}
import model.{Currency, User, Stock}
import module.data.{StockDao, UserDao}
import org.joda.time.DateTime

@Singleton
class UserService @Inject() (userDao: UserDao, stockTransactionService: StockTransactionService, stockDao: StockDao) {

    /**
     * Sell stock, remove the stock from user, get a new stock on the market to update price and sell it.
     */
    def sell(userStockId: String, userId: String, rate: Double): Option[String] = {
        val user: User = userDao.find(userId).get
        user.getStockByUserStockId(userStockId) match {
            case Some(stock) => {
                val currency: Currency = Currency(stock.name, rate)
                val newStock =  stock.copy(currency = currency)
                user.removeStock(userStockId)
                Some(stockTransactionService.transact(newStock, "SELL", user.name))
            }
            case None => None
        }
    }


    def sell(userStockId: String, userId: String, tradeDate: DateTime, quantity: Int): Option[String] = {
        val user: User = userDao.find(userId).get
        user.getStockByUserStockId(userStockId) match {
            case Some(userStock) => {
                if (userStock.quantity > 0) {
                    stockDao.findBy(userStock.name, tradeDate) match {
                        case Some(tradeStock) => {
                            var quantityUsed = quantity
                            if ((userStock.quantity - quantity) <= 0) {
                                quantityUsed = userStock.quantity
                            }
                            user.removeStock(userStockId)
                            val lossFor: BigDecimal = (tradeStock.price * quantityUsed * tradeStock.currency.rate) - (userStock.price * quantityUsed * userStock.currency.rate)
                            user.addStock(userStock.copy(quantity = (userStock.quantity - quantityUsed), profitLoss = lossFor))
                            val newStock =  tradeStock.copy(userStockId = userStockId, quantity = quantityUsed, profitLoss = lossFor)
                            Some(stockTransactionService.transact(newStock, "SELL", user.name))
                        }
                        case None => None
                    }
                } else {
                    None
                }

            }
            case None => None
        }
    }


    def buy(stockId: String, userId: String, quantity: Int): Option[String] = {
        stockDao.findBy(stockId) match {
            case Some(stock) => {
                val userStockId: String = UUID.randomUUID().toString
                val newStock: Stock = stock.copy(userStockId = userStockId, quantity = quantity)
                val user: User = userDao.find(userId).get
                user.addStock(newStock)
                Some(stockTransactionService.transact(newStock, "BUY", userId))
            }
            case None => None
        }
    }

    def getUser(name: String): Option[User] = {
        userDao.find(name)
    }
}
