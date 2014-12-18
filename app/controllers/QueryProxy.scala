package controllers

import assets.QueryFromRequest
import play.api.Play.current
import play.api._
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits._


object QueryProxy extends Controller {

    case class QueryResponse2(Response: Response)
    case class Response(TimeUnit: List[Double], stats: Stats)
    case class Stats(data: List[Data])
    case class Data(identifier: Identifier, metric: List[Metric])
    case class Identifier(names: List[String], values: List[String])
    case class Metric(env: String, name: String, values: List[Double])

    implicit val metricReads = Json.reads[Metric]
    implicit val identifierReads = Json.reads[Identifier]
    implicit val dataReads = Json.reads[Data]
    implicit val statsReads = Json.reads[Stats]
    implicit val responseReads = Json.reads[Response]
    implicit val queryResponse2Reads = Json.reads[QueryResponse2]


    def query (path: String) = Action.async { request =>

        val targetUrl = Play.current.configuration.getString("target.server").get
        val targetBasePath = Play.current.configuration.getString("target.basePath").get

        // Creates a funes request instance.
        val q = QueryFromRequest.get(path, request.queryString)

        Logger.debug(q.Uri(targetUrl + targetBasePath))
        val holder: WSRequestHolder = WS.url(q.Uri(targetUrl + targetBasePath))
            .withHeaders(AUTHORIZATION -> request.headers.get("Authorization").get)


        holder.get().map { response =>

            val jsonResult = response.json.validate[QueryResponse2]


            jsonResult match {
                case s: JsSuccess[QueryResponse2] =>
                    Logger.debug(s.get.Response.TimeUnit.length.toString)

                case e: JsError =>
                    Logger.debug("Error!!")
            }

            Result(
                header = ResponseHeader(response.status, Map(CONTENT_TYPE -> response.header("Content-Type").get)),
                body = Enumerator(response.body.getBytes())
            )
        }
    }

}