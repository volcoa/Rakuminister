package controllers

import play.api._
import play.api.mvc._
import shared.SharedMessages

object Marketplace extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.home(SharedMessages.itWorks))
  }

  def search = Action { implicit request =>
    Ok(views.html.home(SharedMessages.itWorks))
  }
}
