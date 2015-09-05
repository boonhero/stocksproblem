package module.service

import com.google.inject.{Inject, Singleton}
import model.{User, Stock}
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
    def sell(stock: Stock, userId: String): Option[String] = {
        getUser(userId) match {
            case Some(user) => {
                user.removeStock(stock._id)
                userDao.update(user)
                stockDao.findBy(stock.name, new DateTime()) match {
                    case None => None
                    case newStock => Some(stockTransactionService.transact(stock, "SELL", user.name))
                }

            }
            case None => None
        }
    }


    def buy(stock: Stock, userId: String): Option[String] = {
        getUser(userId) match {
            case Some(user) => {
                user.addStock(stock)
                userDao.update(user)
                Some(stockTransactionService.transact(stock, "BUY", user.name))
            }
            case None => None
        }
    }

    def getUser(name: String): Option[User] = {
        userDao.find(name)
    }
}
