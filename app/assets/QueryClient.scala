package assets

import controllers.QueryProxy._
import play.api.Play
import play.api.libs.ws._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._


import scala.concurrent.Future

/**
 * Created by oscar on 12/19/14.
 */
object QueryClient {

    def get (query: Query, request: Request[AnyContent]): Future[TimeSeriesData] = {

        val targetUrl = Play.current.configuration.getString("target.server").get
        val targetBasePath = Play.current.configuration.getString("target.basePath").get
        val url = query.Uri(targetUrl + targetBasePath)
        val holder: WSRequestHolder = WS.url(url)
            .withHeaders(AUTHORIZATION -> request.headers.get("Authorization").get)

        holder.get().map { response =>

            TimeSeriesDataValidator.parse(response.json)

        }
    }

}
