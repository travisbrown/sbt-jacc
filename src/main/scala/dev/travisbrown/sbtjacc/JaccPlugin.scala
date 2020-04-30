package dev.travisbrown.sbtjacc

import java.io.File
import sbt._
import sbt.Keys._

object JaccPlugin extends AutoPlugin {
  object autoImport {
    val jacc = taskKey[Unit]("Generate JFlex lexers and Jacc parsers")
    val jaccSource = settingKey[File]("Jacc source directory")
    val jaccOutput = settingKey[File]("Jacc output directory")

    val jflexSource = settingKey[File]("JFlex source directory")
    val jflexOutput = settingKey[File]("JFlex output directory")
  }

  import autoImport._

  override lazy val projectSettings = inConfig(Compile)(
    Seq(
      jaccSource := baseDirectory.value / "src" / "main" / "jacc",
      jaccOutput := baseDirectory.value / "target" / "jacc",
      jflexSource := baseDirectory.value / "src" / "main" / "jacc",
      jflexOutput := baseDirectory.value / "target" / "jacc",
      jacc := run(jaccSource.value, jaccOutput.value, jflexSource.value, jflexOutput.value),
      unmanagedSourceDirectories.in(Compile) += jaccOutput.value,
      unmanagedSourceDirectories.in(Compile) += jflexOutput.value,
      compileOrder := CompileOrder.JavaThenScala
    )
  )

  def run(jaccSource: File, jaccOutput: File, jflexSource: File, jflexOutput: File): Unit = {
    val jflexSourceFinder = jflexSource * "*.flex"
    val jaccSourceFinder = jaccSource * "*.jacc"
    val jaccOutputFinder = jaccSource * "*.java"

    jflexSourceFinder.get.foreach { source =>
      val args = Array[String](
        "-d", s"${jflexOutput.getAbsolutePath}",
        source.getAbsolutePath
      )
      jflex.Main.main(args)
    }

    jaccSourceFinder.get.foreach { source =>
      val args = Array[String](source.getAbsolutePath)
      dev.travisbrown.jacc.CommandLine.main(args)
    }

    jaccOutputFinder.get.foreach { source =>
      val target = new File(jaccOutput, source.getName)
      IO.move(source, target)
    }
  }
}

