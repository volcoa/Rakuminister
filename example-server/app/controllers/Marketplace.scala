package controllers

import java.io.InputStream

import models.{NavigationResult, ProductInfo}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._
import webservices.PMWebServices

object Marketplace extends Controller {

  // URI : "/"
  def index = Action.async { implicit request =>

    PMWebServices.cmsWS()
      .map(
        result => {
          val responseBody: String = result.body
          val jahiaRepsonseAsXml = scala.xml.XML.withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser()).loadString(responseBody)
          Ok(views.html.home((jahiaRepsonseAsXml \\ "body").toString()))
        }
      )
  }

  // URI : "/s/:keyword"
  def search(keyword: String, pageNumber: Int, advertType: String, ajax: Boolean) = Action.async { implicit request =>

    PMWebServices.searchWS(keyword, pageNumber, advertType)
      .map(
        result => {
          var error:Boolean = false
          var errorMessage:String = ""
          val navigationResultAsJson = (result.json \ "result").validate[NavigationResult]
          val navigationResult : NavigationResult = navigationResultAsJson match {
            case JsSuccess(navigationResult : NavigationResult, _) => navigationResult
            case e: JsError => {
              error = true
              errorMessage = e.toString
              null
            }
          }
          if(error){
            Ok(views.html.oups(errorMessage))
          }else{
            if(ajax){
              Ok(views.html.tags.productListing(keyword, navigationResult))
            }else{
              Ok(views.html.search(keyword, navigationResult))
            }
          }
        }
      );
  }

  // URI : "/offer/buy/:productId/"
  def productInfo(productId: Long, advertType: String, other: String="") = Action.async { implicit request =>
    PMWebServices.productInfoWS(productId, advertType)
      .map(
        result => {
          var error:Boolean = false
          var errorMessage:String = ""
          val productInfoAsJson = (result.json \ "result").validate[ProductInfo]

          val productInfo:ProductInfo = productInfoAsJson match {
              case JsSuccess(productInfo: ProductInfo, _) => productInfo
              case e: JsError => {
                error = true
                errorMessage = e.toString
                null
              }
            }
          if(error){
            Ok(views.html.oups(errorMessage))
          }else{
            Ok(views.html.product(productInfo))
          }
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

 /* def computeCategories(): Unit ={
    PMWebServices.categoriesWS()
      .map(
        result => {
          val json = result.json
          json.
          Ok(result.json.toString())
        }
      );
  }*/
}