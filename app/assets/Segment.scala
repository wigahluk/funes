package assets

import java.text.SimpleDateFormat
import java.util.TimeZone

import com.github.nscala_time.time.Imports._
import play.utils.UriEncoding

/**
 * Created by oscar on 12/17/14.
 */
sealed trait Segment {
    def contains (s: Segment): Boolean
    def contains (p: Long): Boolean
    def intersects (s: Segment): Boolean
    def starts (p: Long): Boolean
    def ends (p: Long): Boolean
}

object Segment {
    case object NoSegment extends Segment {
        override def contains(p: Long): Boolean = false

        override def contains (s: Segment): Boolean = false

        override def intersects(s: Segment): Boolean = false

        override def starts(p: Long): Boolean = false

        override def ends(p: Long): Boolean = false
    }
    case class Empty(At: Long) extends Segment {
        override def contains(p: Long): Boolean = At == p

        override def contains(s: Segment): Boolean = s match {
            case NoSegment => true
            case Empty(x) => contains(x)
            case Cons(x,y) => false
        }

        override def intersects(s: Segment): Boolean = contains(s)

        override def starts(p: Long): Boolean = contains(p)

        override def ends(p: Long): Boolean = contains(p)
    }
    case class Cons(From: Long, To: Long) extends Segment {
        override def contains(p: Long): Boolean = From <= p && p <= To

        override def contains(s: Segment): Boolean = s match {
            case NoSegment => true
            case Empty(x) => contains(x)
            case Cons(x,y) => contains(x) && contains(y)
        }

        override def intersects(s: Segment): Boolean = s match {
            case NoSegment => true
            case Empty(x) => contains(x)
            case Cons(x,y) => contains(x) || contains(y)
        }

        override def starts(p: Long): Boolean = p == From

        override def ends(p: Long): Boolean = p == To
    }

    def length(s: Segment): Long = s match {
        case NoSegment => 0L
        case Empty(_) => 0L
        case Cons(x,y) => y - x
    }

    def encodePoint(p: Long): String = {
        val dFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
        dFormat.setTimeZone(TimeZone.getTimeZone("GMT"))
        UriEncoding.encodePathSegment(dFormat.format(p), "UTF-8")
    }

    def encodePoints(ps: List[Long]): String = ps.map(encodePoint).mkString("~")

    def uriEncodeSegment (s: Segment): String = s match {
        case NoSegment => ""
        case Empty(x) => encodePoints(List(x, x))
        case Cons(x,y) => encodePoints(List(x, y))
    }

    implicit class SegmentUtils(s: Segment) {
        def uriEncode: String = uriEncodeSegment (s)
        def length: Long = Segment.length(s)
    }

    def apply(start:DateTime, end:DateTime): Segment = apply(start.getMillis, end.getMillis)
    def apply(start:Long, end:Long) = {
        val diff = end - start
        diff match {
            case 0 => Empty(start)
            case d => if (d < 0) NoSegment else Cons(start, end)
        }
    }
}