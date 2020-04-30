organization in ThisBuild := "dev.travisbrown"

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
  scalacOptions in (Compile, console) ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  },
  scalacOptions in (Test, console) ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  }
)

val allSettings = baseSettings ++ publishSettings

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(allSettings)
  .settings(
    name := "sbt-jacc",
    javaSource.in(Compile) := baseDirectory.value / "jacc" / "src",
    libraryDependencies += "de.jflex" % "jflex" % "1.8.1"
  )


lazy val publishSettings = Seq(
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  homepage := Some(url("https://github.com/travisbrown/sbt-jacc")),
  licenses := Seq("GNU General Public License v3" -> url("https://www.gnu.org/licenses/gpl-3.0.en.html")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
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

