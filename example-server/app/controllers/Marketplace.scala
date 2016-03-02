package controllers

import models.{Product, ProductInfo}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._
import shared.SharedMessages
import webservices.PMWebServices

object Marketplace extends Controller {

  // URI : "/"
  def index = Action { implicit request =>
    Ok(views.html.home(SharedMessages.itWorks))
  }

  // URI : "/s/:keyword"
  def search(keyword: String, pageNumber: Int, advertType: String) = Action.async { implicit request =>

    PMWebServices.searchWS(keyword, pageNumber, advertType)
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

  // URI : "/offer/buy/:productId/"
  def productInfo(productId: Long, advertType: String) = Action.async { implicit request =>
    PMWebServices.productInfoWS(productId, advertType)
      .map(
        result => {
          val productInfo = (result.json \ "result").validate[ProductInfo].get
          Ok(views.html.product(productInfo))
        }
      );
  }

  // URI : "/s/:keyword"
  def completion(keyword: String) = Action.async { implicit request =>

    PMWebServices.completionWS(keyword)
      .map(
        result => {
          Ok(result.json.toString())
        }
      );
  }
}