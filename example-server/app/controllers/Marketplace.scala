package controllers

import models.Product
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.{WSResponse, WS}
import play.api.mvc._
import shared.SharedMessages

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import models.PMWsResult

object Marketplace extends Controller {

  implicit val resultReads = Json.reads[PMWsResult]
//  implicit val productReads = Json.reads[Product]

  // URL : "/"
  def index = Action { implicit request =>
    Ok(views.html.home(SharedMessages.itWorks, null))
  }

  // URL : "/s/:keyword"
  def search(keyword: String) = Action.async { implicit request =>

    searchWS(keyword)
      .map(
        result => {
          Ok(views.html.home("Test 1/2", (result.json \ "result").validate[PMWsResult].get))
        }
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
//        "category" -> "Mode",
        "pageNumber" -> "1",
        "advertType" -> "ALL",
        "loadProducts" -> "true",
        "withoutStock" -> "false",
        "loadAdverts" -> "false")
      .get()
  }
}
