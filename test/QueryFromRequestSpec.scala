import assets.QueryFromRequest
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

/**
 * Created by oscar on 12/17/14.
 * Given a http request, this service builds a query object that will be used as a start point.
 */
@RunWith(classOf[JUnitRunner])
class QueryFromRequestSpec extends Specification {

    "QueryFromRequest" should {

        "Build a Query" in {
            val q = QueryFromRequest.Get("path", Map("k1" -> List("v1")));
            q.QueryId mustEqual "path?k1=v1"
        }

        "Build a Query with range should not include range in queryId" in {
            val q = QueryFromRequest.Get("path", Map("k1" -> List("v1"), "timeRange" -> List("11/24/2014 17:00:00~11/25/2014 17:00:00")));
            q.QueryId mustEqual "path?k1=v1"
        }

        "Build a Query with range" in {
            val q = QueryFromRequest.Get("path", Map("k1" -> List("v1"), "timeRange" -> List("11/24/2014 17:00:00~11/25/2014 17:00:00")));
            q.Range.From mustEqual 1416848400000L
            q.Range.To mustEqual 1416934800000L
        }

        "Encode range from query" in {
            val q = QueryFromRequest.Get("path", Map("k1" -> List("v1"), "timeRange" -> List("11/24/2014 17:00:00~11/25/2014 17:00:00")));
            q.Range.UriEncode mustEqual "11%2F24%2F2014%2017:00:00~11%2F25%2F2014%2017:00:00"
        }

        "URI from query" in {
            val q = QueryFromRequest.Get("path", Map("k1" -> List("v1"), "timeRange" -> List("11/24/2014 17:00:00~11/25/2014 17:00:00")));
            q.Uri mustEqual "path?k1=v1&timeRange=11%2F24%2F2014%2017:00:00~11%2F25%2F2014%2017:00:00"
        }

        "URI with prefix from query" in {
            val q = QueryFromRequest.Get("path", Map("k1" -> List("v1"), "timeRange" -> List("11/24/2014 17:00:00~11/25/2014 17:00:00")));
            q.Uri("prefix") mustEqual "prefixpath?k1=v1&timeRange=11%2F24%2F2014%2017:00:00~11%2F25%2F2014%2017:00:00"
        }
    }
}
