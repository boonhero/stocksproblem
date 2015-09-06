package controllers

import java.util.UUID

import com.google.inject.Inject
import model._
import module.data.mock.MockStockProvider
import module.data.{StockDao, StockTransactionDao, UserDao}
import module.service.{UserService, StockTransactionService}
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import utility.DateHelper

import scala.util.Random

class StockController @Inject() (userService: UserService, stockTransactionService: StockTransactionService, stockDao: StockDao, mockStockProvider: MockStockProvider, stockTransactionDao: StockTransactionDao, userDao: UserDao) extends Controller {
  val logger: Logger = Logger(this.getClass())

  implicit val currencyFormat = Json.format[Currency]
  implicit val stockFormat = Json.format[Stock]
  implicit val computeResultFormat = Json.format[ComputeResult]
  implicit val stockTransactionFormat = Json.format[StockTransaction]

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
    mockStockProvider.readStocks()
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

    userService.buy(stockId, userId, quantity) match {
      case Some(userTransactionId) => Redirect(routes.StockController.myDashboardIndex)
      case None => BadRequest("not a valid request")
    }

    Redirect(routes.StockController.myDashboardIndex)

  }

  /**
   *
   * @param date accepts dd-MM-yyyy
   * @return
   */
  def findStocksByDate(date: String)  = Action {implicit request =>
    logger.info(date)
    val stocks: List[Stock] = stockDao.findBy(DateHelper.dashFormatter.parseDateTime(date))
    val json: JsValue = Json.toJson(stocks)
    logger.info(s"json result:  [${json}]")
    Ok(json)
  }

  /**
   *
   * @param userStockId
   * @param tradeDate - accepts dd-MM-yyyy
   * @param quantity
   * @return
   */
  def sell(userStockId:String, tradeDate:String, quantity: Int) = Action {implicit request =>
    userService.sell(userStockId, "testId", DateHelper.dashFormatter.parseDateTime(tradeDate), quantity) match {
      case Some(str) =>  Redirect(routes.StockController.myDashboardIndex)
      case None => Redirect(routes.StockController.myDashboardIndex)
    }

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

  def profitLoss(userStockId: String) = Action { implicit request =>
    Ok(stockTransactionService.getProfitOrLossFor(userStockId).toString())
  }

  def showStockStatus = Action { implicit request =>
    val computeResults: List[ComputeResult] = stockTransactionService.getProfitOrLossForAllStocks()
    val json: JsValue = Json.toJson(computeResults)
    logger.info(s"json result:  [${json}]")
    Ok(json)
  }

  def resetAll = Action { implicit request =>
    stockTransactionDao.removeAll()
    userDao.find("testId") match {
      case Some(user) => user.removeAllStocks()
    }
    Redirect(routes.StockController.myDashboardIndex)
  }

  def resetTransactions = Action { implicit request =>
    stockTransactionDao.removeAll()
    Redirect(routes.StockController.myDashboardIndex)
  }

  def listStockTransactions = Action { implicit request =>
    val json: JsValue = Json.toJson( stockTransactionDao.findAll().get)
    logger.info(s"json result:  [${json}]")
    Ok(json)
  }

}
