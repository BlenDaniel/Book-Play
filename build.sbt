name := """book-inventory-management"""
organization := "book.management"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.16"

libraryDependencies ++= Seq(
  guice,
  javaJpa,
  "com.h2database" % "h2" % "2.2.224",
  "org.hibernate" % "hibernate-core" % "6.4.4.Final",
  "org.hibernate.validator" % "hibernate-validator" % "8.0.1.Final",
  "javax.validation" % "validation-api" % "2.0.1.Final",
  "org.playframework" %% "play-test" % "3.0.8" % Test,
  "org.assertj" % "assertj-core" % "3.24.2" % Test
)
