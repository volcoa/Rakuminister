package controllers

import _root_.util.WSCall
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

/*  implicit val productReads: Reads[Product] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "urlName").read[String] and
      (JsPath \ "bestPrice").read[Double] and
      (JsPath \ "newBestPrice").read[Double] and
      (JsPath \ "usedBestPrice").read[Double] and
      (JsPath \ "collectibleBestPrice").read[Double] and
      (JsPath \ "advertsCount").read[Int] and
      (JsPath \ "advertsNewCount").read[Int] and
      (JsPath \ "advertsUsedCount").read[Int] and
      (JsPath \ "advertsCollectibleCount").read[Int] and
      (JsPath \ "headline").read[String] and
      //(JsPath \ "caption").read[String] and
      (JsPath \ "topic").read[String] and
      (JsPath \ "reviewsAverageNote").read[Double] and
      (JsPath \ "nbReviews").read[Long] and
      (JsPath \ "imagesUrls").read[Seq[String]] and
      //(JsPath \ "attributes").read[String] and
      (JsPath \ "isMemo").read[Boolean]
    )(Product.apply _)*/


  // URL : "/"
  def index = Action { implicit request =>
    Ok(views.html.home(SharedMessages.itWorks))
  }

  // URL : "/s/:keyword"
  def search(keyword: String, pageNumber: Int) = Action.async { implicit request =>
    searchWS(keyword, pageNumber)
      .map(
        result => {
          var error:Boolean = false
          var errorMessage:String = ""
          val json = result.json \ "result"
          val jsonArrayProducts = (json \ "products").validate[List[Product]]
          val products:List[Product] = jsonArrayProducts match {
            case JsSuccess(list : List[Product], _) => list
            case e: JsError => {
              error = true
              errorMessage = e.toString
              List()
            }
          }
          if(error){
            Ok(views.html.oups(errorMessage))
          }else{
            Ok(views.html.search(keyword, products))
          }
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
  def searchWS(keyword: String, pageNumber: Int) : Future[WSResponse] = {

    WS.url(WSCall.getNextServer() + "/rest/navigation/v1/list")
    //WS.url("http://ws.priceminister.com/rest/navigation/v1/list")
      .withHeaders("Accept" -> "application/json")
      .withQueryString(
        "kw" -> keyword,
//        "category" -> "Mode",
        "pageNumber" -> pageNumber.toString,
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