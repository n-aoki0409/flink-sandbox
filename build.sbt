scalaVersion := "2.12.11"

name := "flink-sandbox"
organization := "example.com"
version := "1.0"

val flinkVersion = "1.13.2"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.apache.flink" %% "flink-clients" % flinkVersion,
  "org.apache.flink" %% "flink-runtime-web" % flinkVersion,
)
