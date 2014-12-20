import assets.TimeRange
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

/**
 * Created by oscar on 12/19/14.
 */
@RunWith(classOf[JUnitRunner])
class TimeRangeSpec extends Specification {

    "TimeRange" should {

        "Encode itself to URI param" in {
            val tr = TimeRange(1416848400000L, 1416934800000L)
            tr.UriEncode mustEqual "11%2F24%2F2014%2017:00:00~11%2F25%2F2014%2017:00:00"
        }

        "R1 properly contains R2" in {
            val t1 = 1416848400000L;
            val t2 = t1 + 1000*60*60*24
            val t3 = t1 + 1000*60*60*2
            val t4 = t1 + 1000*60*60*10
            val tr1 = TimeRange(t1, t2)
            val tr2 = TimeRange(t3, t4)
            tr1.Contains(tr2) mustEqual true
            tr1.Intersects(tr2) mustEqual true
        }

        "R1 contains R2" in {
            val t1 = 1416848400000L;
            val t2 = t1 + 1000*60*60*24
            val t3 = t1
            val t4 = t1 + 1000*60*60*10
            val tr1 = TimeRange(t1, t2)
            val tr2 = TimeRange(t3, t4)
            tr1.Contains(tr2) mustEqual true
            tr1.Intersects(tr2) mustEqual true
        }

        "R1 not contains R2 but intersects it" in {
            val t1 = 1416848400000L;
            val t2 = t1 + 1000*60*60*24
            val t3 = t1 - 1000*60*60*2
            val t4 = t1 + 1000*60*60*10
            val tr1 = TimeRange(t1, t2)
            val tr2 = TimeRange(t3, t4)
            tr1.Contains(tr2) mustEqual false
            tr1.Intersects(tr2) mustEqual true
        }

        "R1 not intersects R2" in {
            val t1 = 1416848400000L;
            val t2 = t1 + 1000*60*60*24
            val t3 = t1 - 1000*60*60*48
            val t4 = t1 - 1000*60*60*24
            val tr1 = TimeRange(t1, t2)
            val tr2 = TimeRange(t3, t4)
            tr1.Contains(tr2) mustEqual false
            tr1.Intersects(tr2) mustEqual false
        }
    }
}
