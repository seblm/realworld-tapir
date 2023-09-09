lazy val root = project
  .in(file("."))
  .settings(
    name := "realworld-tapir",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := "3.3.1",

    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % "1.7.3",
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe" % "0.20.2",
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.7.3",
    libraryDependencies += "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.6.0"
  )
