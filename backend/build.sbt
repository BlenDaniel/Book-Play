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
  "jakarta.persistence" % "jakarta.persistence-api" % "3.1.0",
  "javax.persistence" % "javax.persistence-api" % "2.2",
  "jakarta.validation" % "jakarta.validation-api" % "3.0.2",
  "javax.validation" % "validation-api" % "2.0.1.Final",
  // Testing dependencies
  "org.playframework" %% "play-test" % "3.0.8" % Test,
  "org.assertj" % "assertj-core" % "3.24.2" % Test,
  "org.mockito" % "mockito-core" % "5.8.0" % Test,
  "org.mockito" % "mockito-junit-jupiter" % "5.8.0" % Test,
  "org.junit.jupiter" % "junit-jupiter-engine" % "5.10.1" % Test,
  "org.junit.jupiter" % "junit-jupiter-api" % "5.10.1" % Test,
  "org.junit.platform" % "junit-platform-suite-api" % "1.10.1" % Test,
  "org.junit.platform" % "junit-platform-suite-engine" % "1.10.1" % Test,
  "org.hamcrest" % "hamcrest" % "2.2" % Test
)
