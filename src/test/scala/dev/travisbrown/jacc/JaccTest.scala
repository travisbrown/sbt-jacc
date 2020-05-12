package dev.travisbrown.jacc

import java.io.InputStream
import munit.FunSuite
import scala.io.Source

class JaccTest extends FunSuite {
  test("Calc") {
    val args = Array(
      "-v",
      "-o", "build",
      "-e", "src/test/resources/dev/travisbrown/jacc/Calc.errs",
      "src/test/resources/dev/travisbrown/jacc/Calc.jacc"
    )
    CommandLine.main(args)

    val parser = Source.fromFile("build/CalcParser.java").getLines.toList
    val parserExpected = getLines("CalcParser.java")

    val tokens = Source.fromFile("build/CalcTokens.java").getLines.toList
    val tokensExpected = getLines("CalcTokens.java")

    val output = Source.fromFile("build/Calc.output").getLines.toList
    val outputExpected = getLines("CalcParser.output")

    assertEquals(parser, parserExpected)
    assertEquals(tokens, tokensExpected)
    assertEquals(output, outputExpected)
  }

  private def getLines(path: String): List[String] = Source.fromInputStream(this.getResource(path)).getLines.toList

  private def getResource(path: String): InputStream = {
    this.getClass.getClassLoader.getResource(s"dev/travisbrown/jacc/$path").openStream()
  }
}
