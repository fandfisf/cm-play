package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class HomeController @Inject() extends Controller {

  def scrape = Action {
    Ok(views.html.scrape("Scrape away."))
  }

}
