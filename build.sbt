name := "kafka-connect-opentsdb"

version := "0.0.1"

scalaVersion := "2.11.8"

lazy val versions = Map(
  "kafka" -> "0.10.0.0",
  "play" -> "2.4.6",
  "scalaTest" -> "3.0.0-RC3"
)

libraryDependencies ++= Seq(
  "org.apache.kafka" % "connect-api" % versions("kafka"),
  ("com.typesafe.play" %% "play-ws" % versions("play")).exclude("commons-logging", "commons-logging"),
  "com.typesafe.play" %% "play-test" % versions("play") % "test",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
  "de.leanovate.play-mockws" %% "play-mockws" % "2.4.2" % "test",
  "org.scalactic" %% "scalactic" % versions("scalaTest"),
  "org.scalatest" %% "scalatest" % versions("scalaTest") % "test"
)

resolvers ++= Seq(
  "Confluent" at "http://packages.confluent.io/maven/"
)