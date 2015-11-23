package assets

/**
 * Created by oscar on 12/17/14.
 */
case class TimeSeriesBucket (Data: TimeSeriesData) {
    var Range: Segment = Segment(Data.Response.TimeUnit.head, Data.Response.TimeUnit.last)
    def CanSolve(query: Query): Boolean = Range.contains(query.Range)

}
