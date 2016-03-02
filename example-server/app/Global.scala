import com.typesafe.config.ConfigFactory
import play.api._
import play.api.mvc._
import play.filters.csrf._
import util.WSCall

object Global extends WithFilters(CSRFFilter()) with GlobalSettings {

  val props = ConfigFactory.load("hackaton.properties")

  override def onStart(app: play.api.Application) {
    WSCall.createServersList(props.getString("servers.list.file.path"))
  }

}
