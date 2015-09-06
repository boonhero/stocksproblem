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
     *
     * @param stock
     * @param userId
     * @return
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
