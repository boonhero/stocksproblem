package controllers

import com.google.inject.Inject
import model.{ComputeResult, Currency, Stock, User}
import module.data.mock.MockStockProvider
import module.data.{StockDao, StockTransactionDao, UserDao}
import module.service.StockTransactionService
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import scala.util.Random

class StockController @Inject() (stockTransactionService: StockTransactionService, stockDao: StockDao, mockStockProvider: MockStockProvider, stockTransactionDao: StockTransactionDao, userDao: UserDao) extends Controller {
  val logger: Logger = Logger(this.getClass())

  implicit val currencyFormat = Json.format[Currency]
  implicit val stockFormat = Json.format[Stock]
  implicit val computeResultFormat = Json.format[ComputeResult]

  def index = Action {
    mockStockProvider.readStocks()
    logger.info("stocks index")
    Ok(views.html.stocks("Your stocks"))
  }

  def buystockIndex(stockId: String) = Action {
    logger.info("buy stock index")
    Ok(views.html.buystock(stockId))
  }

  def myDashboardIndex = Action {
    logger.info("buy stock index")
    Ok(views.html.dashboard(""))
  }

  def findOne(id: String) = Action {
    logger.info(s"find one stock ${id}")
    stockDao.findBy(id) match {
      case Some(stock) => Ok(Json.toJson(stock))
      case None => NotFound
    }
  }

  val buyStockTuple = Form(
    tuple(
      "userId" -> text,
      "stockId" -> text,
      "quantity" -> number
    )
  )

  def list = Action { implicit request =>
    logger.info(s"list")
    val stocks: List[Stock] = stockDao.findAll()
        val json: JsValue = Json.toJson(stocks)
        Ok(json)
  }

  def buyTransaction = Action { implicit request =>
    logger.info("test buy transaction")
    logger.info("request: " + request.body)
    val (userId, stockId, quantity) = (request.body.asFormUrlEncoded.get("userId")(0), request.body.asFormUrlEncoded.get("stockId")(0), request.body.asFormUrlEncoded.get("quantity")(0).toInt)

    stockDao.findBy(stockId) match {
      case Some(stock) => {
        val newStock: Stock = stock.copy(quantity = quantity)
        val user: User = userDao.find(userId).get
        user.addStock(newStock)
        stockTransactionService.transact(newStock, "BUY", userId)

      }
      case None => BadRequest("not a valid request")
    }
    Redirect(routes.StockController.myDashboardIndex)
  }

  def showStocksFrom(userId: String) = Action { implicit request =>
    logger.info(s"user stocks list: ${userId}")
    val stocks: List[Stock] = userDao.find(userId) match {
      case Some(user) => user.stocks
    }
    val json: JsValue = Json.toJson(stocks)
    logger.info(s"json result:  [${json}]")
    Ok(json)
  }

  def showCurrencyRates = Action { implicit request =>
    logger.info(s"showCurrencyRates")
    val stock: Stock = Random.shuffle(stockDao.findAll()).head
    val currencies: List[Currency] = stockDao.findBy(stock.tradeDate).map(f => f.currency)
    val json: JsValue = Json.toJson(currencies)
    logger.info(s"CURRENCY json result:  [${json}]")
    Ok(json)
  }

  def showStockStatus = Action { implicit request =>
    val computeResults: List[ComputeResult] = stockTransactionService.getProfitOrLossForAllStocks()
    val json: JsValue = Json.toJson(computeResults)
    logger.info(s"json result:  [${json}]")
    Ok(json)
  }


}
