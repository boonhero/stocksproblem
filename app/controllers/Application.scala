package controllers

import model.{Message, Stock}
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

class Application extends Controller {
  val logger: Logger = Logger(this.getClass())

  implicit val messageFormat = Json.format[Message]
  implicit val stockFormat = Json.format[Stock]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def createBox = Action {
    logger.info("create your box")
    Ok(views.html.createBox("Create your box"))
  }

  val messageForm = Form(
    mapping(
      "id" -> ignored("": String),
      "message" -> nonEmptyText
    )(Message.apply)(Message.unapply)
  )

  /**
   * Retrieves all messages
   */
  def messageList = Action { implicit request =>
    logger.info("message list")
    val json: JsValue = Json.toJson(Message.all())
    logger.info(s"json result:  [${json}]")
    Ok(json)
  }

  /**
   * Handle Message submission form
   */
  def saveMessage = Action { implicit request =>
    logger.info("saveMessage")
    messageForm.bindFromRequest.fold(
      formWithErrors => BadRequest(""),
      message => {
        Message.create(message)
        Ok("")
      })
  }

}
