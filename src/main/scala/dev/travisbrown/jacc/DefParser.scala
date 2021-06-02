package dev.travisbrown.jacc

import dev.travisbrown.jacc.grammar.Grammar
import fastparse._

sealed abstract class Definition extends Product with Serializable

object Definition {
  case class Package(value: String) extends Definition
  case class Class(value: String) extends Definition
  case class Interface(value: String) extends Definition
  case class Semantic(value: String) extends Definition
  case class Start(value: String) extends Definition
  case class Code(value: String) extends Definition
  case class Type(value: Option[String], names: List[String]) extends Definition
  case class Token(value: Option[String], names: List[String]) extends Definition
  case class Left(values: List[String]) extends Definition
  case class Right(values: List[String]) extends Definition
}

case class Production(name: String, alts: List[(List[String], Option[String])])

case class GrammarDef(definitions: List[Definition], productions: List[Production], postCode: String) {
  private val fixities: Map[String, Fixity] = definitions
    .foldLeft((Map.empty[String, Fixity], 0)) {
      case ((fs, prec), Definition.Right(names)) => (fs ++ names.map((_, Fixity.right(prec))), prec + 1)
      case ((fs, prec), Definition.Left(names))  => (fs ++ names.map((_, Fixity.left(prec))), prec + 1)
      case (acc, _)                              => acc
    }
    ._1

  private val nonTerminalTypes: Map[String, String] = definitions.flatMap {
    case Definition.Type(Some(tpe), names) => names.map((_, tpe))
    case _                                 => Nil
  }.toMap

  private val startSymbolName: Option[String] = definitions.collectFirst { case Definition.Start(name) =>
    name
  }

  private val Literal = "'(.)'".r

  private val nonTerminals: List[JaccSymbol] = {
    var seqNo = 0

    val prodSyms = productions.map { case Production(name, alts) =>
      val prods = alts.map { case (syms, action) =>
        seqNo += 1
        new JaccProd(syms.toArray, seqNo, action)
      }.toArray

      JaccSymbol(name, -1, -1, None, nonTerminalTypes.get(name), prods)
    }

    startSymbolName match {
      case None => prodSyms.head :: prodSyms.tail.sortBy(_.name)
      case Some(name) =>
        val (List(start), rest) = prodSyms.partition(_.name == name)

        start :: rest.sortBy(_.name)
    }
  }

  private val literals: List[JaccSymbol] = definitions
    .flatMap {
      case Definition.Token(tpe, names) =>
        names.collect { case name @ Literal(c) =>
          JaccSymbol(name, c.charAt(0).toInt, -1, fixities.get(name), tpe, Array.empty)
        }
      case _ => Nil
    }
    .sortBy(_.name)

  private def getNextNum(n: Int): Int = if (literals.map(_.num).contains(n)) getNextNum(n + 1) else n

  private val terminals: List[JaccSymbol] = (
    definitions.flatMap {
      case Definition.Token(tpe, names) =>
        names.flatMap {
          case name @ Literal(c) => None
          case name              => Some(JaccSymbol(name, -1, -1, fixities.get(name), tpe, Array.empty))
        }
      case _ => Nil
    } :+ JaccSymbol("error", -1, -1, None, None, Array.empty)
  ).foldLeft((List.empty[JaccSymbol], 1)) { case ((acc, num), sym) =>
    val nextNum = getNextNum(num)
    (acc :+ sym.copy(num = nextNum), nextNum + 1)
  }._1
    .sortBy(_.name)

  lazy val getGrammar: Grammar = {
    val arr: Array[JaccSymbol] =
      (nonTerminals ++ terminals ++ literals :+ JaccSymbol("$end", 0, -1, None, None, Array.empty)).zipWithIndex.map {
        case (sym, i) => sym.copy(tokenNo = i)
      }.toArray

    new Grammar(arr, nonTerminals.map(_.prods).toArray)
  }

  def updateSettings(settings: Settings): Settings = {
    settings.addPostText(postCode)
    definitions.foreach {
      case Definition.Code(value)      => settings.addPreText(value)
      case Definition.Class(value)     => settings.setClassName(value)
      case Definition.Interface(value) => settings.setInterfaceName(value)
      case Definition.Package(value)   => settings.setPackageName(value)
      //case Definition.Extends(value) => settings.setExtendsName(value)
      //case Definition.Implements(value) => settings.setImplementsName(value)
      case Definition.Semantic(value) => settings.setTypeName(value)
      //case Definition.GetToken(value) => settings.setGetToken(value)
      //case Definition.NextToken(value) => settings.setNextToken(value)
      case _ =>
    }

    settings
  }

  def parseErrorExamples(input: String): java.lang.Iterable[(String, Array[Int])] = {
    import scala.collection.JavaConverters._

    fastparse.parse(input, GrammarDefParser.errorExamples(_)) match {
      case Parsed.Success(values, _) =>
        values.flatMap { case (name, alts) =>
          alts.map { alt =>
            (name, alt.map(s => getGrammar.lookup(s).tokenNo).toArray)
          }
        }.asJava
    }
  }
}

object GrammarDefParser {
  import Definition._

  implicit private val whitespace: ParsingRun[_] => ParsingRun[Unit] =
    JavaWhitespace.whitespace.andThen(MultiLineWhitespace.whitespace)

  def parseFile(path: String): GrammarDef = {
    val stream = new java.io.FileInputStream(path)

    fastparse.parse(stream, jaccFile(_)) match {
      case Parsed.Success(value, _) => value
    }
  }

  def jaccFile[_: P]: P[GrammarDef] =
    P(fastparse.Start ~ definition.rep ~ "%%" ~ production.rep ~ "%%" ~~ AnyChar.repX.! ~~ fastparse.End).map {
      case (ds, ps, rest) => GrammarDef(ds.toList, ps.toList, rest)
    }

  private def definition[_: P]: P[Definition] =
    packageDef | classDef | interfaceDef | semanticDef | startDef | codeDef | typeDef | tokenDef | leftDef | rightDef

  private def packageDef[_: P]: P[Definition] = P("%package" ~ javaIdentifier).map(Package(_))
  private def classDef[_: P]: P[Definition] = P("%class" ~ javaIdentifier).map(Class(_))
  private def interfaceDef[_: P]: P[Definition] = P("%interface" ~ javaIdentifier).map(Interface(_))
  private def semanticDef[_: P]: P[Definition] = P("%semantic" ~ javaType).map(Semantic(_))
  private def startDef[_: P]: P[Definition] = P("%start" ~ javaType).map(Start(_))

  private def codeDef[_: P]: P[Definition] = P("%{" ~~ (!"%}" ~~ AnyChar).repX.! ~~ "%}").map(Code(_))
  private def typeDef[_: P]: P[Definition] = P("%type" ~ ("<" ~ javaType ~ ">").? ~ javaIdentifier.!.rep).map {
    case (t, ps) => Type(t, ps.toList)
  }
  private def tokenDef[_: P]: P[Definition] = P("%token" ~ ("<" ~ javaType ~ ">").? ~ symbol.!.rep).map {
    case (t, ps) => Token(t, ps.toList)
  }

  private def leftDef[_: P]: P[Definition] = P("%left" ~ symbol.!.rep).map(xs => Left(xs.toList))
  private def rightDef[_: P]: P[Definition] = P("%right" ~ symbol.!.rep).map(xs => Right(xs.toList))

  private def production[_: P]: P[Production] = P(javaIdentifier ~ ":" ~ alt.rep(sep = "|") ~ ";").map {
    case (name, alts) => Production(name, alts.toList)
  }

  private def alt[_: P]: P[(List[String], Option[String])] = P(symbol.!.rep ~ ("{" ~ javaCode ~ "}").!.?).map {
    case (ps, code) => (ps.toList, code)
  }

  private def symbol[_: P]: P[String] =
    P(javaIdentifier | ("'" ~~ AnyChar ~~ "'").!)

  private def javaIdentifier[_: P]: P[String] = P(
    (CharPred(Character.isJavaIdentifierStart) ~~ CharsWhile(Character.isJavaIdentifierPart).repX)
      .repX(
        min = 1,
        sep = "."
      )
      .!
  )

  private def javaGenericParam[_: P]: P[String] = P("<" ~ javaType.rep(min = 1, sep = ",").! ~ ">")
  private def javaType[_: P]: P[String] = P((javaIdentifier ~ javaGenericParam.?).!)

  private def plainJavaCode[_: P]: P[String] = P((!"{" ~ !"}" ~ AnyChar).rep(1).!)
  private def javaCode[_: P]: P[String] = P((plainJavaCode | ("{" ~ javaCode.rep ~ "}")).rep(1).!)

  private def errorExampleMessage[_: P]: P[String] = P("\"" ~ ("\\\"" | (!"\"" ~ AnyChar)).rep.! ~ "\"")
  private def errorExample[_: P]: P[(String, List[List[String]])] =
    P(errorExampleMessage ~ ":" ~ (symbol.!.rep).rep(sep = "|") ~ ";").map { case (message, alts) =>
      (message, alts.map(_.toList).toList)
    }

  def errorExamples[_: P]: P[List[(String, List[List[String]])]] =
    P(fastparse.Start ~ errorExample.rep ~ fastparse.End).map(_.toList)
}
