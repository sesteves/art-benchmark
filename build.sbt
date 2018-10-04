
name := "art-benchmark"

version := "0.1"

scalaVersion := "2.10.7"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-streaming" % "1.3.1",
  "org.apache.spark" %% "spark-mllib" % "1.3.1",
  "org.java-websocket" % "Java-WebSocket" % "1.3.9",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)