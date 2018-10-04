
name := "art-benchmark"

version := "0.1"

scalaVersion := "2.10.7"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-streaming" % "1.3.1",
  "org.apache.spark" %% "spark-mllib" % "1.3.1",
  "org.glassfish.tyrus" % "tyrus-server" % "1.14",
  "javax.websocket" % "javax.websocket-api" % "1.1" % "provided",
  "org.glassfish.tyrus" % "tyrus-container-grizzly-server" % "1.14",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)