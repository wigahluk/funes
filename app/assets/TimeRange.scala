package assets

import java.text.SimpleDateFormat
import java.util.TimeZone

import play.utils.UriEncoding

/**
 * Created by oscar on 12/17/14.
 */
case class TimeRange ( From: Long, To: Long) {

    // Given R1 = [a1, b1] and R2 = [a2, b2], returns true iif (a1 <= a2 and a2 <= b1) or (a1 <= b2 and b2 <= b1)
    def Intersects(range: TimeRange) = (From <= range.From && range.From <= To) || (From <= range.To && range.To <= To)

    // Given R1 = [a1, b1] and R2 = [a2, b2], returns true iif a1 <= a2 and b1 >= b2
    def Contains (range: TimeRange) = From <= range.From && To >= range.To

    def UriEncode : String = {
        val dFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
        dFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        UriEncoding.encodePathSegment(Seq(From, To).map(d => dFormat.format(d)).mkString("~"), "UTF-8")
    }
}
