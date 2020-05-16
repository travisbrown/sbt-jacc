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
  private val symbolList: List[(String, JaccSymbol)] = {
    definitions.collect {
      case Definition.Token(_, names) => names
    }.flatten ++ productions.map(_.name)
  }.map {
    case name if name.startsWith("'") => (name, new JaccSymbol(name, name.charAt(1).toInt))
    case name => (name, new JaccSymbol(name))
  }

  private val symbols: Map[String, JaccSymbol] = symbolList.toMap

  private var precedence = 0

  definitions.collect {
    case Definition.Right(names) =>
      names.foreach(name => symbols(name).setFixity(Fixity.right(precedence)))
      precedence += 1
    case Definition.Left(names) =>
      names.foreach(name => symbols(name).setFixity(Fixity.left(precedence)))
      precedence += 1
    case Definition.Type(Some(tpe), names) =>
      names.foreach(name => symbols(name).setType(tpe))
  }

  val literals: List[JaccSymbol] = symbolList.collect {
    case (name, sym) if name.startsWith("'") => sym
  }

  private var tokenNo = 1

  val terminals: List[JaccSymbol] = symbolList.collect {
    case (name, sym) if !name.startsWith("'") =>
      while (literals.exists(_.getNum == tokenNo)) {
        tokenNo += 1
      }

      sym.setNum(tokenNo)
      sym
  }

  val nonTerminals: List[JaccSymbol] = {
    var seqNo = 0

    val prods = productions.map {
      case Production(name, alts) =>
        val sym = symbols(name)

        alts.map {
          case (syms, action) =>
            seqNo += 1

            sym.addProduction(new JaccProd(null, syms.map(symbols(_)).toArray, null, action.orNull, seqNo))
        }

        sym
    }

    val start = definitions.collectFirst {
      case Definition.Start(name) => symbols(name)
    }

    start match {
      case Some(startSym) =>
        startSym :: prods.filterNot(_.eq(startSym))
      case None => prods
    }
  }

  def getGrammar: Grammar = {
    val arr: Array[Grammar.Symbol] = (nonTerminals ++ terminals ++ literals :+ new JaccSymbol("$end", 0)).toArray

    arr.zipWithIndex.foreach {
      case (sym: JaccSymbol, i) => sym.setTokenNo(i)
      case _ =>
    }

    new Grammar(arr, nonTerminals.map { case j: JaccSymbol => j.getProds }.toArray.asInstanceOf[Array[Array[Grammar.Prod]]])
  }
}

object GrammarDefParser {
  import Definition._

  private implicit val whitespace: ParsingRun[_] => ParsingRun[Unit] =
    JavaWhitespace.whitespace.andThen(MultiLineWhitespace.whitespace)

  def jaccFile[_: P]: P[GrammarDef] =
    P(fastparse.Start ~ definition.rep ~ "%%" ~ production.rep ~ "%%" ~ AnyChar.rep.! ~ fastparse.End).map {
      case (ds, ps, rest) => GrammarDef(ds.toList, ps.toList, rest)
    }

  private def definition[_: P]: P[Definition] =
    packageDef | classDef | interfaceDef | semanticDef | startDef | codeDef | typeDef | tokenDef | leftDef | rightDef

  private def packageDef[_: P]: P[Definition] = P("%package" ~ javaIdentifier).map(Package(_))
  private def classDef[_: P]: P[Definition] = P("%class" ~ javaIdentifier).map(Class(_))
  private def interfaceDef[_: P]: P[Definition] = P("%interface" ~ javaIdentifier).map(Interface(_))
  private def semanticDef[_: P]: P[Definition] = P("%semantic" ~ javaType).map(Semantic(_))
  private def startDef[_: P]: P[Definition] = P("%start" ~ javaType).map(Start(_))

  private def codeDef[_: P]: P[Definition] = P("%{" ~ (!"%}" ~ AnyChar).rep.! ~ "%}").map(Code(_))
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

  private def alt[_: P]: P[(List[String], Option[String])] = P(symbol.!.rep ~ ("{" ~ javaCode ~ "}").?).map {
    case (ps, code) => (ps.toList, code)
  }

  private def symbol[_: P]: P[String] =
    P(javaIdentifier | ("'" ~~ AnyChar ~~ "'").!)

  private def javaIdentifier[_: P]: P[String] =
    P((CharPred(Character.isJavaIdentifierStart) ~~ CharsWhile(Character.isJavaIdentifierPart).repX).repX(min = 1, sep = ".").!)

  private def javaGenericParam[_: P]: P[String] = P("<" ~ javaType.rep(min = 1, sep = ",").! ~ ">")
  private def javaType[_: P]: P[String] = P((javaIdentifier ~ javaGenericParam.?).!)

  private def plainJavaCode[_: P]: P[String] = P((!"{" ~ !"}" ~ AnyChar).rep(1).!)
  private def javaCode[_: P]: P[String] = P((plainJavaCode | ("{" ~ javaCode.rep ~ "}")).rep(1).!)
}
