package controllers

import java.util.Date

import model.{StockTransaction, Message, Stock}
import org.bson.types.ObjectId
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

class StockController extends Controller {
  val logger: Logger = Logger(this.getClass())

  implicit val stockFormat = Json.format[Stock]

  def index = Action {
    logger.info("stocks index")
    Ok(views.html.stocks("Your stocks"))
  }

  val buyStockForm = Form(
    mapping(
      "id" -> ignored("": String),
      "stock" -> mapping (
        "" -> ignored("": String),
        "name" -> nonEmptyText
      )(Stock.apply)(Stock.unapply),
      "price" -> number,
      "orderedDate" -> date,
      "transactionType" -> default(text, "BUY": String),
      "transactionDate" -> default(date, new Date(): Date),
      "userId" -> nonEmptyText
    )(StockTransaction.apply)(StockTransaction.unapply)
  )

  /**
   * Handle Buy stocks form
   */
  def buyTransaction = Action { implicit request =>
    logger.info("buyTransaction")
    buyStockForm.bindFromRequest.fold(
      formWithErrors => BadRequest(""),
      stockTransaction => {
        logger.info(s"stocktransaction: ${stockTransaction.toString}")
        Stock.findByName(stockTransaction.stock.name) match {
          case Some(stock) => {
            logger.info(s"found match for ${stockTransaction.stock.name}")
            stockTransaction.stock = stock
            StockTransaction.create(stockTransaction)
          }
          case None => {
            logger.info(s"no match for ${stockTransaction.stock.name}")
            var stockId: ObjectId = Stock.create(stockTransaction.stock)
            StockTransaction.create(stockTransaction)
          }
        }
        Ok("")
      })
  }

  def showStocksFrom(userId: String) = Action { implicit request =>
    logger.info(s"user stocks list: ${userId}")
    val stocks: List[Stock] = StockTransaction.findUser(userId).map(stockTransaction => stockTransaction.stock)
    val json: JsValue = Json.toJson(stocks)
    logger.info(s"json result:  [${json}]")
    Ok(json)
  }

}
