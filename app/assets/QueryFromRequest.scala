package assets

import java.text.{DecimalFormat, SimpleDateFormat}
import java.util.{Date, NoSuchElementException}

import scala.collection.SortedMap

/**
 * Created by oscar on 12/17/14.
 */
object QueryFromRequest {
    def Get (path: String, queryString: Map[String, Seq[String]]) : Query = {
        //Build the query ID string from the base path and the query string.
        val queryId = path + "?" + (SortedMap.empty[String, Seq[String]] ++ queryString.filter(_._1 != "timeRange"))
            .map(k => k._1 + "=" + k._2.mkString).mkString("&")

        // Parse the time zone offset, required to build the range.
        var tzo = " +0000"

        try {
            val temp = queryString("tzo").mkString.toInt
            val formatter = new DecimalFormat("0000")
            tzo = formatter.format((temp / 60) * 100 + (temp % 60))
            if(temp > 0) { tzo = " +" + tzo }
            else {tzo = " " + tzo }
        } catch {
            case e : NoSuchElementException => { tzo = " +0000" }
        }

        def range (vs: Option[Seq[String]]): Seq[Long] = vs match {
            case Some(s) => s.mkString.split("~").map(sDate => new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z").parse(sDate + tzo).getTime)
            case None => Seq(new Date().getTime, new Date().getTime - (1000 * 60 * 60))
        }

        val rangeArray = range(queryString.get("timeRange"))

        Query(queryId, Segment(rangeArray(0),rangeArray(1)))
    }

}

case class Query ( QueryId: String, Range: Segment ) {
    def Uri (prefix:String): String = prefix + QueryId + "&timeRange=" + Range.uriEncode
    def Uri (): String = Uri("")
}


