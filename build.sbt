import play.PlayScala

name := "funes"

version := "0.1"

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.11.1")

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
)

libraryDependencies ++= Seq (
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)