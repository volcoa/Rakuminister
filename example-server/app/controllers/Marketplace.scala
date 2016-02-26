package controllers

import play.api.Play.current
import play.api.libs.ws.{WSResponse, WS}
import play.api.mvc._
import shared.SharedMessages

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

object Marketplace extends Controller {

  // URL : "/"
  def index = Action { implicit request =>
    Ok(views.html.home(SharedMessages.itWorks))
  }

  // URL : "/s/:keyword"
  def search(keyword: String) = Action.async { implicit request =>
      searchWS(keyword)
        .map(
          result => Ok(views.html.home(result.body))
        );
  }




  // http://ws.priceminister.com/rest/navigation/v1/list
  //        ?kw=hello
  //        &category=Mode
  //        &pageNumber=1
  //        &advertType=ALL
  //        &channel=buyerapp
  //        &loadProducts=true
  //        &withoutStock=false
  def searchWS(keyword: String) : Future[WSResponse] = {
    WS.url("http://ws.priceminister.com/rest/navigation/v1/list")
      .withHeaders("Accept" -> "application/json")
      .withQueryString(
        "kw" -> keyword,
        //        "category" -> "true",
        "pageNumber" -> "1",
        "advertType" -> "ALL",
        "loadProducts" -> "true",
        "withoutStock" -> "false")
      .get()
  }
}
