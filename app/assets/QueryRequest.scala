package assets

import java.text.{DecimalFormat, SimpleDateFormat}
import java.util.NoSuchElementException

import play.api.mvc.{AnyContent, Request}


/**
 * Created by oscar on 11/28/14.
 */
class QueryRequest (path: String, request: Request[AnyContent]) {
    val rawUri: String = if (request.rawQueryString.length >= 0) {
        path + "?" + request.rawQueryString
    } else {
        path
    }

    var tzo = " +0000"

    try {
        var temp = request.queryString("tzo").mkString.toInt
        val formatter = new DecimalFormat("0000")
        tzo = formatter.format((temp / 60) * 100 + (temp % 60))
        if(temp > 0) { tzo = " +" + tzo }
        else {tzo = " " + tzo }
    } catch {
        case e : NoSuchElementException => { tzo = " +0000" }
    }
    var range = request.queryString("timeRange").mkString.split("~").map { sDate => new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z").parse(sDate + tzo).getTime}
    var unit = request.queryString("timeUnit").mkString
}
