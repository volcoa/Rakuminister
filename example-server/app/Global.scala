import com.typesafe.config.ConfigFactory
import play.api._
import play.api.mvc._
import play.filters.csrf._
import webservices.PMWebServices

object Global extends WithFilters(CSRFFilter()) with GlobalSettings {

  val props = ConfigFactory.load("hackaton.properties")

  override def onStart(app: play.api.Application) {
    PMWebServices.createServersList(props.getString("servers.list.file.path"))
  }

}
