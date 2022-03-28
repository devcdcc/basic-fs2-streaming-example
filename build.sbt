ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "sensor-metrics",
    idePackagePrefix := Some("com.github.devcdcc"),
    libraryDependencies += "co.fs2" %% "fs2-core" % "3.2.5",
    libraryDependencies += "co.fs2" %% "fs2-io" % "3.2.5",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test
  )
