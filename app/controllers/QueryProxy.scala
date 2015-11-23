package controllers

import assets._
import play.api.Play.current
import play.api._
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.libs.ws._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future


object QueryProxy extends Controller {

    def query (path: String) = Action.async { request =>

        // Creates a funes query instance.
        val q = QueryFromRequest.Get(path, request.queryString)
        
        val b = TimeSeriesLake.Find(q.QueryId)

        b match {
            case Some(s) => Future (Ok(TimeSeriesDataValidator.toJson(s.Data)))

            case None => QueryClient.get(q, request).map { r =>
                val bucket = TimeSeriesBucket(r)
                TimeSeriesLake.Store(bucket)
                Ok(TimeSeriesDataValidator.toJson(r))
            }
        }

    }

}