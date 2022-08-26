val scala3Version = "3.1.3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "feda",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    scalacOptions ++= List("-source:future"),

    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.3.14",
      "io.circe" %% "circe-core" % "0.14.2",
      "com.github.julien-truffaut" %% "monocle-core" % "3.0.0-M6",
      "eu.timepit" %% "refined" % "0.10.1",
      "eu.timepit" %% "refined-cats" % "0.10.1",
      "io.circe" %% "circe-refined" % "0.14.2",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
