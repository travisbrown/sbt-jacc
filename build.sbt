ThisBuild / organization := "dev.travisbrown"
ThisBuild / scalaVersion := "2.12.14"

val compilerOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Yno-adapted-args",
  "-Ywarn-unused-import"
)

val baseSettings = Seq(
  scalacOptions ++= compilerOptions,
  Compile / console / scalacOptions ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  },
  Test / console / scalacOptions ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  }
)

val allSettings = baseSettings ++ publishSettings

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(allSettings)
  .settings(
    name := "sbt-jacc",
    Compile / javaSource := baseDirectory.value / "jacc" / "src",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "fastparse" % "2.3.2",
      "de.jflex" % "jflex" % "1.8.2"
    ),
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.26" % Test,
    testFrameworks += new TestFramework("munit.Framework")
  )

lazy val publishSettings = Seq(
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  homepage := Some(url("https://github.com/travisbrown/sbt-jacc")),
  licenses := Seq("GNU General Public License v3" -> url("https://www.gnu.org/licenses/gpl-3.0.en.html")),
  publishMavenStyle := true,
  Test / publishArtifact := false,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots".at(nexus + "content/repositories/snapshots"))
    else
      Some("releases".at(nexus + "service/local/staging/deploy/maven2"))
  },
  autoAPIMappings := true,
  apiURL := Some(url("https://github.com/travisbrown/sbt-jacc")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/travisbrown/sbt-jacc"),
      "scm:git:git@github.com:travisbrown/sbt-jacc.git"
    )
  ),
  developers := List(
    Developer(
      "travisbrown",
      "Travis Brown",
      "travisrobertbrown@gmail.com",
      url("https://twitter.com/travisbrown")
    )
  )
)
