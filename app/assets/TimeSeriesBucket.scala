package assets

/**
 * Created by oscar on 12/17/14.
 */
case class TimeSeriesBucket (Data: TimeSeriesData) {
    var Range: TimeRange = TimeRange(Data.Response.TimeUnit.head, Data.Response.TimeUnit.last)
    def CanSolve(query: Query): Boolean = Range.Contains(query.Range)

}
