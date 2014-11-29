package controllers

import play.api.Play.current
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.Enumerator
import play.api.libs.ws._
import play.api.mvc._

object QueryProxy extends Controller {

  def query (path: String) = Action.async {

    var targetUrl = Play.current.configuration.getString("target.server").get
    var targetBasePath = Play.current.configuration.getString("target.basePath").get
    var queryPath = targetUrl + targetBasePath + path

    var holder : WSRequestHolder = WS.url(queryPath)

    holder.get().map { response =>
      Logger.debug("Estatus headers: " + response.allHeaders)
      Result(
        header = ResponseHeader(response.status, Map(CONTENT_TYPE -> response.header("Content-Type").get)),
        body = Enumerator(response.body.getBytes())
      )
    }
  }

}