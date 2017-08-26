name := "MachineLearningOnStreams"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.1.1"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.1.1"
libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.1.1"
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.5.0"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.4" % "test"
