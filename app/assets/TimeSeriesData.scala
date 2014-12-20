package assets

import play.api.libs.json._
import play.api._
import play.api.Play.current

/**
 * Created by oscar on 11/30/14.
 */
case class validateTimeSeriesResponseException(message:String)  extends Exception

object TimeSeriesDataValidator {
    def parse (response: JsValue) : TimeSeriesData = {

        val jsonResult = response.validate[TimeSeriesData]

        jsonResult match {
            case s: JsSuccess[TimeSeriesData] => s.get
            case e: JsError => throw new validateTimeSeriesResponseException(e.errors.mkString)
        }
    }

    def toJson(timeSeries: TimeSeriesData): JsValue = {
        Json.toJson(timeSeries)
    }

    implicit val metricReads = Json.reads[Metric]
    implicit val identifierReads = Json.reads[Identifier]
    implicit val dataReads = Json.reads[Data]
    implicit val statsReads = Json.reads[Stats]
    implicit val responseReads = Json.reads[Response]
    implicit val timeSeriesDataReads = Json.reads[TimeSeriesData]

    implicit val metricWrites = Json.writes[Metric]
    implicit val identifierWrites = Json.writes[Identifier]
    implicit val dataWrites = Json.writes[Data]
    implicit val statsWrites = Json.writes[Stats]
    implicit val responseWrites = Json.writes[Response]
    implicit val timeSeriesDataWrites = Json.writes[TimeSeriesData]
}

case class TimeSeriesData (Response: Response)
case class Response(TimeUnit: List[Long], stats: Stats)
case class Stats(data: List[Data])
case class Data(identifier: Identifier, metric: List[Metric])
case class Identifier(names: List[String], values: List[String])
case class Metric(env: String, name: String, values: List[Double])
