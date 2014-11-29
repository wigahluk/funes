package controllers

import assets.QueryRequest
import play.api.Play.current
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee.Enumerator
import play.api.libs.ws._
import play.api.mvc._

object QueryProxy extends Controller {

    def query (path: String) = Action.async { request =>

        var targetUrl = Play.current.configuration.getString("target.server").get
        var targetBasePath = Play.current.configuration.getString("target.basePath").get

        var qr = new QueryRequest(path, request)


        var queryPath = targetUrl + targetBasePath + qr.rawUri

        Logger.debug("Request URL: " + queryPath)
        Logger.debug("Request QueryString: " + request.rawQueryString)
        Logger.debug("Range: " + qr.range.mkString("~"))
        Logger.debug("Unit: " + qr.unit)
        Logger.debug("tzo: " + qr.tzo)

        val holder: WSRequestHolder = WS.url(queryPath)
            .withHeaders(AUTHORIZATION -> request.headers.get("Authorization").get)

        holder.get().map { response =>
            Result(
                header = ResponseHeader(response.status, Map(CONTENT_TYPE -> response.header("Content-Type").get)),
                body = Enumerator(response.body.getBytes())
            )
        }
    }

}