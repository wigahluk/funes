import assets._
import com.github.nscala_time.time.Imports._
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

/**
  * Created by oscar on 12/19/14.
  */
@RunWith(classOf[JUnitRunner])
class TimeRangeSpec extends Specification {

    "TimeRange" should {
        "Trivial segment" in {
            val s = Segment(1416848400000L, 1416848300000L)
            s.length mustEqual 0
            s.contains(1416848400000L) mustEqual false
            s.contains(1416848300000L) mustEqual false
        }

        "Encode itself to URI param" in {
            val tr = Segment(1416848400000L, 1416934800000L)
            tr.uriEncode mustEqual "11%2F24%2F2014%2017:00:00~11%2F25%2F2014%2017:00:00"
        }

        "Ranges can be created from dates" in {
            val end = DateTime.now
            val start = end - 1.days
            val r = Segment(start, end)
            r.starts(start.getMillis) mustEqual true
            r.ends(end.getMillis) mustEqual true
        }

        "R1 properly contains R2" in {
            val t1 = 1416848400000L
            val t2 = t1 + 1000*60*60*24
            val t3 = t1 + 1000*60*60*2
            val t4 = t1 + 1000*60*60*10
            val tr1 = Segment(t1, t2)
            val tr2 = Segment(t3, t4)
            tr1.contains(tr2) mustEqual true
            tr1.intersects(tr2) mustEqual true
        }

        "R1 contains R2" in {
            val t1 = 1416848400000L
            val t2 = t1 + 1000*60*60*24
            val t3 = t1
            val t4 = t1 + 1000*60*60*10
            val tr1 = Segment(t1, t2)
            val tr2 = Segment(t3, t4)
            tr1.contains(tr2) mustEqual true
            tr1.intersects(tr2) mustEqual true
        }

        "R1 not contains R2 but intersects it" in {
            val t1 = 1416848400000L
            val t2 = t1 + 1000*60*60*24
            val t3 = t1 - 1000*60*60*2
            val t4 = t1 + 1000*60*60*10
            val tr1 = Segment(t1, t2)
            val tr2 = Segment(t3, t4)
            tr1.contains(tr2) mustEqual false
            tr1.intersects(tr2) mustEqual true
        }

        "R1 not intersects R2" in {
            val t1 = 1416848400000L
            val t2 = t1 + 1000*60*60*24
            val t3 = t1 - 1000*60*60*48
            val t4 = t1 - 1000*60*60*24
            val tr1 = Segment(t1, t2)
            val tr2 = Segment(t3, t4)
            tr1.contains(tr2) mustEqual false
            tr1.intersects(tr2) mustEqual false
        }

        "Length of a segment" in {
            val s = Segment(1416848400000L, 1416848500000L)
            s.length mustEqual 100000L
        }
    }
}
