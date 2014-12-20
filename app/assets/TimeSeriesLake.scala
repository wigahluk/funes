package assets

/**
 * Created by oscar on 12/19/14.
 */
object TimeSeriesLake {
    def Find (queryId: String): Option[TimeSeriesBucket] = ts
    def Store (bucket: TimeSeriesBucket) = { ts = Option(bucket) }

    private var ts: Option[TimeSeriesBucket] = None

}
