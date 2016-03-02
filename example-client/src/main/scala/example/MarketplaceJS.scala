package example

import config.Routes
import org.scalajs.dom
import org.scalajs.jquery.{jQuery => $, JQueryXHR}
import scala.collection.mutable.ListBuffer
import scala.scalajs.js
import scala.language.dynamics


import org.scalajs.dom
import org.scalajs.dom.{Element}
import org.scalajs.dom.html.Div

import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

import upickle.default._

import dom.ext._
import scala.scalajs
.concurrent
.JSExecutionContext
.Implicits
.runNow

@JSExport
object MarketplaceJS extends js.JSApp {

  def $id(s: String) = dom.document.getElementById(s)

  @JSExport
  override def main(): Unit = {
    redirectToSearchPageEventInit()
    callSuggestEventInit()
    removeSuggestWhenUnBlurEventInit()
  }

  @JSExport
  def redirectToSearchPageEventInit() = {
    $("#main-navbar-search-form").on("submit", (e: dom.Event) => {
      e.preventDefault()
      val keyword = $("#searchInput").value
      dom.document.location.href = "/s/" + keyword
    });
  }

  @JSExport
  def callSuggestEventInit() = {
    $("#searchInput").on("keyup", (e: dom.Event) => {
      CompletionJS.main(dom.document.getElementById("suggestContainer"))
    });
  }

  @JSExport
  def removeSuggestWhenUnBlurEventInit() = {
    var mouseOverSuggestList = false

    $("#searchInput").on("blur", (e: dom.Event) => {
      if(!mouseOverSuggestList){
        $("#suggestContainer").html("");
      }
    });

    /*$("document").on("click", (e: dom.Event) => {
      if(!mouseOverSuggestList){
        $("#suggestContainer").html("");
      }
    });*/

    $("#suggestContainer").on("mouseover", (e: dom.Event) => {
      mouseOverSuggestList = true
    });

    $("#suggestContainer").on("mouseout", (e: dom.Event) => {
      mouseOverSuggestList = false
    });
  }
}

@JSExport
object NavigationAjaxJS extends {

  def fetchData(keyword: String,advType: String, pageNumber: Int) = Ajax.get(Routes.NavigationAjax.get(keyword, advType, pageNumber))

  @JSExport
  def getProducts(keyword: String, advType: String, pageNumber: Int): Unit = {
      fetchData(keyword, advType, pageNumber).onSuccess {
        case s =>
          val html = s.responseText
          if(pageNumber == 1){
            $("#productListing").html(html)
          }else{
            $("#productListing").append(html)
          }
       }
  }

}


@JSExport
object CompletionJS extends {

  var suggestedKeywords = List[String]()

  @JSExport
  def main(target: Element): Unit = {
    val keyword = $("#searchInput").value.toString
    fetchData(keyword).onSuccess {
      case s =>
        println(s)
        val suggestResult = read[List[String]](s.responseText)
        val kwBuff = ListBuffer[String]()
        for(i<-2 until suggestResult.size by 2){
          kwBuff += suggestResult(i).substring(3)
        }
        suggestedKeywords = kwBuff.toList

        refreshScreen(target)
    }
  }

  def fetchData(keyword: String) = Ajax.get(Routes.Completion.get(keyword))

  def refreshScreen(target: Element): Unit = {
    target.innerHTML = ""
    target.appendChild(
      rebuildUI(target)
    )

  }

  def rebuildUI(target: Element): Div =
    div(
      ul(
        for (kw <- suggestedKeywords) yield
          li(createLink(kw)), `class` := "list-group"
      )
    ).render

  def createLink(kw: String) = {
    val suggestedWordLink = a(kw, `href`:="/s/" + kw, `class`:="sugg_link").render
    /*suggestedWordLink.onclick = (_: MouseEvent) => {
      dom.document.location.href = "/s/" + kw
    }*/
    suggestedWordLink
  }

}


