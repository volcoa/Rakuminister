package webservices

import play.api.libs.ws.{WS, WSResponse}
import play.api.Play.current
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

object PMWebServices {

  var serversList = List[String]()
  var serverIndex = 0

  def createServersList(serverListFilePath: String): Unit ={
    var servers = new ListBuffer[String]()
    for (serverLine <- scala.io.Source.fromFile(serverListFilePath).getLines()) {
      servers += serverLine
    }
    serversList = servers.toList
  }

  def getNextServer(): String = {
    if(serverIndex < serversList.length-1){
      serverIndex = serverIndex + 1
    }else {
      serverIndex = 0
    }
    var server = serversList(0)
    try {
      server = serversList(serverIndex)
    } catch {
      case e: Exception => server = serversList(0)
    }
    return server
  }

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

    WS.url(getNextServer() + "/rest/navigation/v1/list")
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

    WS.url(getNextServer() + "/rest/product/v1/get")
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

  // "http://cms-dev-01:8080/cms/render/default/fr/sites/mobwebapp/seocategories.html"
  def categoriesWS() : Future[WSResponse] = {

    WS.url("http://cms-dev-01:8080/cms/render/default/fr/sites/mobwebapp/seocategories.html")
      .withHeaders("Accept" -> "application/json")
      .get()
  }


  // http://cms-dev-01:8080/cms/render/live/fr/sites/priceminister/home/hackathon-play.html
  def cmsWS() : Future[WSResponse] = {

    WS.url("http://cms-dev-01:8080/cms/render/live/fr/sites/priceminister/home/hackathon-play.html")
      .withHeaders("Accept" -> "text/html")
      .get()
  }
}
