name := """book-inventory-management"""
organization := "book.management"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.13.16"

libraryDependencies ++= Seq(
  guice,
  javaJdbc,
  "com.h2database" % "h2" % "2.2.224",
  "org.projectlombok" % "lombok" % "1.18.30" % "provided",
  javaTestKit % Test,
  "org.assertj" % "assertj-core" % "3.24.2" % Test
)
