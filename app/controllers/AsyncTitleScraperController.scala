package controllers

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.actor.ActorSystem
import javax.inject.Inject
import javax.inject.Singleton
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.JsPath
import play.api.libs.json.Json
import play.api.libs.json.Writes
import play.api.libs.ws.WSClient
import play.api.mvc.Action
import play.api.mvc.Controller

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asychronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.
 */
case class Title(site: String, title: String)
case class Error(message: String, id: Long)
@Singleton
class AsyncTitleScraperController @Inject() ( ws: WSClient, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {

  val titlePattern = "(<[Tt][Ii][Tt][Ll][Ee]>)(.*)(</[Tt][Ii][Tt][Ll][Ee]>)".r

  implicit val titleWrites: Writes[Title] = (
    (JsPath \ "site").write[String] and
    (JsPath \ "title").write[String])(unlift(Title.unapply))

  implicit val errorWrites: Writes[Error] = (
    (JsPath \ "errorMessage").write[String] and
    (JsPath \ "id").write[Long])(unlift(Error.unapply))
/*
 
 ws.url(url).get().map(response =>
          (titlePattern findFirstMatchIn (response.body)).map(m => Title(url, m.group(2)))) map { x => Ok(Json.toJson(x)) }
      }
 
 
 */
  def title = Action.async { implicit request =>
    val paramsWithFirstValue = request.queryString.map { case (k, v) => k -> v(0) }
    paramsWithFirstValue.get("url") match {
      case Some(url) => {
        ws.url(url).get().map(response =>
          (titlePattern findFirstMatchIn (response.body)).map(m => Title(url, m.group(2)))) map { x => Ok(Json.toJson(x)) }
      }
      case _ => {
        Future { Ok(Json.toJson(Error("No url provided", 1))) }
      }
    }
  }

}



