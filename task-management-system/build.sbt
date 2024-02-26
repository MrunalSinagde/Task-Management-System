name := """task-management-system"""
organization := "com.mrunal"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies ++= Seq(
  guice,
  "org.joda" % "joda-convert" % "2.2.3",
  "io.lemonlabs" %% "scala-uri" % "4.0.3",
  "net.codingwell" %% "scala-guice" % "6.0.0",
  "org.playframework" %% "play-slick" % "6.0.0",
  "org.playframework" %% "play-slick-evolutions" % "6.0.0",
  "mysql" % "mysql-connector-java" % "8.0.33"

)
scalacOptions ++= Seq(
  "-feature"
)
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.mrunal.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.mrunal.binders._"
