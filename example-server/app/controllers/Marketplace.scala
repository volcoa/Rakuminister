package controllers

import java.util.concurrent.TimeoutException

import models.{NavigationResult, ProductInfo}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.mvc._
import webservices.PMWebServices

import scala.xml.Elem

object Marketplace extends Controller {

  // URI : "/"
  def index = Action.async { implicit request =>

    PMWebServices.cmsWS()
      .map(
        result => {
          var error: Boolean = false
          var jahiaRepsonseAsXml: Elem = null

          if (result.status == OK) {
            val responseBody: String = result.body
            jahiaRepsonseAsXml = scala.xml.XML.withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser()).loadString(responseBody)

          } else {
            error = true
            Logger.logger.error("[" + request.uri + "]" + " - Jahia Call Error")
          }

          if (error) {
            Ok(views.html.home(("")))
          } else {
            Ok(views.html.home((jahiaRepsonseAsXml \\ "body").toString()))
          }
        }
      ).recover {
      case _ =>
        Logger.logger.error("An error occured")
        Ok(views.html.home(("")))
    }
  }

  def nav(keyword: String, category: String, pageNumber: Int, advertType: String, ajax: Boolean) = Action.async { implicit request =>
    PMWebServices.navigationWS(keyword, pageNumber, advertType, category)
      .map(
        result => {
          var error:Boolean = false
          var errorMessage:String = ""
          var navigationResult: NavigationResult = null

          if (result.status == OK) {
            val navigationResultAsJson = (result.json \ "result").validate[NavigationResult]
            navigationResult = navigationResultAsJson match {
              case JsSuccess(navigationResult : NavigationResult, _) => navigationResult
              case e: JsError => {
                // Binding error
                error = true
                e.errors.foreach(error => errorMessage += " - " + error._1)
                Logger.logger.error("[" + request.uri + "]" + " - Binding error on " + errorMessage)
                null
              }
            }
          }
          // WS ERROR
          else {
            error = true
            Logger.logger.error("[" + request.uri + "]" + " - " + (result.json \ "code").get + " : " + (result.json \ "message").get)
          }

          if(error) {
            Ok(views.html.oups(errorMessage))
          }
          else {
            navigationResult.isLocalSearch = category != "" && keyword != ""
            navigationResult.isSearch = category == "" && keyword != ""
            navigationResult.isNav = category != "" && keyword == ""
            val splittedCattegories = category.split("_")
            if(splittedCattegories.size > 1){
              navigationResult.formattedCategory = splittedCattegories(splittedCattegories.size - 1).toLowerCase.capitalize
            }else{
              navigationResult.formattedCategory = splittedCattegories(0).toLowerCase.capitalize
            }

            if(ajax) {
              Ok(views.html.tags.productListing(keyword, category, navigationResult))
            }
            else {
              Ok(views.html.search(keyword, category, navigationResult))
            }
          }
        }
      ).recover {
      case _ =>
        Logger.logger.error("An error occured")
        Ok(views.html.oups("Les serveurs de PriceMinister sont en cours de maintenance."))
    };
  }

  // URI : "/offer/buy/:productId/"
  def productInfo(productId: Long, advertType: String, other: String="") = Action.async { implicit request =>

    PMWebServices.productInfoWS(productId, advertType)
      .map(
        result => {
          var error:Boolean = false
          var errorMessage:String = ""
          var productInfo: ProductInfo = null

          if (result.status == OK) {
            val productInfoAsJson = (result.json \ "result").validate[ProductInfo]
            productInfo = productInfoAsJson match {
              case JsSuccess(productInfo: ProductInfo, _) => productInfo
              case e: JsError => {
                // Binding error
                error = true
                e.errors.foreach(error => errorMessage += " - " + error._1)
                Logger.logger.error("[" + request.uri + "]" + " - Binding error on " + errorMessage)
                null
              }
            }
          // WS ERROR
          } else {
            error = true
            Logger.logger.error("[" + request.uri + "]" + " - " + (result.json \ "code").get + " : " + (result.json \ "message").get)
            errorMessage += "Produit introuvable"
          }

          if(error) {
            Ok(views.html.oups(errorMessage))
          } else {
            Ok(views.html.product(productInfo))
          }
        }
      ).recover {
        case _ =>
          Logger.logger.error("An error occured")
          Ok(views.html.oups("Les serveurs de PriceMinister sont en cours de maintenance."))
      }
  }

  // URI : "/completion"
  def completion(keyword: String) = Action.async { implicit request =>

    PMWebServices.completionWS(keyword)
      .map(
        result => {
          if (result.status == OK) {
            Ok(result.json.toString())
          }
          else {
            Logger.logger.error("[" + request.uri + "]" + " - " + (result.json \ "code") + " : " + (result.json \ "message"))
            Ok("")
          }
        }
      ).recover {
      case _ =>
        Logger.logger.error("An error occured")
        Ok("")
    };
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