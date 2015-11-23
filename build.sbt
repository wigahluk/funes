name := """funes"""

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.11.7")

libraryDependencies ++= Seq(
    ws,
    specs2 % Test,
    "com.github.nscala-time" %% "nscala-time" % "2.6.0"
)
