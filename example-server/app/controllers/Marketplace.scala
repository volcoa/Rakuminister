package controllers

import models.{Product, PMWsResult}
import play.api.Play.current
import play.api.libs.ws.{WSResponse, WS}
import play.api.mvc._
import shared.SharedMessages
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

object Marketplace extends Controller {

  //implicit val resultReads = Json.reads[PMWsResult]
  //implicit val requestInfosReads = Json.reads[RequestInfos]

  implicit val productReads: Reads[Product] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "urlName").read[String] and
      (JsPath \ "headline").read[String] and
      (JsPath \ "imagesUrls").read[Seq[String]]
    )(Product.apply _)

  // URL : "/"
  def index = Action { implicit request =>
    Ok(views.html.home(SharedMessages.itWorks))
  }

  // URL : "/s/:keyword"
  def search(keyword: String) = Action.async { implicit request =>

    searchWS(keyword)
      .map(
        result => {
          val json = (result.json \ "result")
          val jsonArrayProducts = (json \ "products").validate[List[Product]]
          val products = jsonArrayProducts match {
            case JsSuccess(list : List[Product], _) => list
            case e: JsError => {List()}
          }
          Ok(views.html.search(keyword, products))
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

  // URL : "/s/:keyword"
  def completion(keyword: String) = Action.async { implicit request =>

    completionWS(keyword)
      .map(
        result => {
          Ok(result.json.toString())
        }
      );
  }

  def completionWS(keyword: String) : Future[WSResponse] = {
    val queryKw = "gs_" + keyword
    WS.url("http://www.priceminister.com/completion")
      .withHeaders("Accept" -> "application/json")
      .withQueryString(
        "q" -> queryKw,
        "c" -> "frc")
      .get()
  }


}