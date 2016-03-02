package webservices

import play.api.libs.ws.{WS, WSResponse}
import play.api.Play.current
import scala.concurrent.Future

object PMWebServices {

  // http://ws.priceminister.com/rest/navigation/v1/list
  //        ?kw=:keyword
  //        &category=Mode
  //        &pageNumber=:pageNumber
  //        &advertType=ALL | NEW | USED | COLLECTIBLE
  //        &channel=buyerapp
  //        &loadProducts=true
  //        &withoutStock=false
  //        &channel=hackathon
  def searchWS(keyword: String, pageNumber: Int, advertType: String) : Future[WSResponse] = {

    WS.url("http://ws.priceminister.com/rest/navigation/v1/list")
      .withHeaders("Accept" -> "application/json")
      .withQueryString(
        "kw" -> keyword,
        //        "category" -> "Mode",
        "pageNumber" -> pageNumber.toString,
        "advertType" -> advertType,
        "loadProducts" -> "true",
        "withoutStock" -> "false",
        "loadAdverts" -> "false",
        "channel" -> "hackathon"
      )
      .get()
  }


  // http://ws.priceminister.com/rest/product/v1/get
  //        ?productId=:productId
  //        &advertType=ALL | NEW | USED | COLLECTIBLE
  //        &loadProductDetails=true
  //        &channel=hackathon
  def productInfoWS(productId: Long, advertType: String) : Future[WSResponse] = {

    WS.url("http://ws.priceminister.com/rest/product/v1/get")
      .withHeaders("Accept" -> "application/json")
      .withQueryString(
        "productId" -> productId.toString,
        "advertType" -> advertType,
        "loadProductDetails" -> "true",
        "channel" -> "hackathon"
      )
      .get()
  }


  // "http://www.priceminister.com/completion"
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
