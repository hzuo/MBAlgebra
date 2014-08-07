package hzuo.mbalgebra.util

import scalaz._
import Scalaz._

object PropLogic {

  sealed trait Formula
  case class Variable(v: Symbol) extends Formula
  case class Not(f: Formula) extends Formula
  case class And(f1: Formula, f2: Formula) extends Formula
  case class Or(f1: Formula, f2: Formula) extends Formula

  val ui: BijectionT[Id, Option, Formula, String] = {
    def show(f: Formula): String = {
      import String._
      f match {
        case Variable(v) => v.name
        case Not(f) => format("!%s", show(f))
        case Or(f1, f2) => format("%s + %s", show(f1), show(f2))
        case And(f1: Or, f2: Or) => format("(%s) * (%s)", show(f1), show(f2))
        case And(f1, f2: Or) => format("%s * (%s)", show(f1), show(f2))
        case And(f1: Or, f2) => format("(%s) * %s", show(f1), show(f2))
        case And(f1, f2) => format("%s * %s", show(f1), show(f2))
      }
    }
    object Read extends scala.util.parsing.combinator.JavaTokenParsers {
      def variable: Parser[Formula] = ident ^^ (Symbol.apply _ andThen Variable.apply _)
      def parenthesized: Parser[Formula] = "(" ~> expr <~ ")"
      def not: Parser[Formula] = "!" ~> one ^^ Not
      def one: Parser[Formula] = variable | parenthesized | not
      def term: Parser[Formula] = one ~ rep("*" ~> one) ^^ { case hd ~ tl => tl.foldLeft(hd)(And) }
      def expr: Parser[Formula] = term ~ rep("+" ~> term) ^^ { case hd ~ tl => tl.foldLeft(hd)(Or) }

      def apply(text: String): Option[Formula] = {
        val prepared = text.filterNot(_.isWhitespace)
        parseAll(expr, prepared) match {
          case Success(f, _) => Some(f)
          case _ => None
        }
      }
    }
    BijectionT.bijection[Id, Option, Formula, String](show, Read.apply)
  }

  def eval(interpret: Variable => Boolean): Formula => Boolean = {
    def evalFormula(f: Formula): Boolean = {
      f match {
        case v: Variable => interpret(v)
        case Not(f) => !evalFormula(f)
        case And(f1, f2) => evalFormula(f1) && evalFormula(f2)
        case Or(f1, f2) => evalFormula(f1) || evalFormula(f2)
      }
    }
    evalFormula
  }

  import Private._

  val dnf: Formula => Either[Boolean, Formula] = dnf2formula compose formula2dnf

  implicit val FormulaEqual = new Equal[Formula] {
    override def equal(a: Formula, b: Formula): Boolean = {
      formula2dnf(a).truthfulAssignments == formula2dnf(b).truthfulAssignments
    }
  }

  private object Private {

    def collectVariables(f: Formula): Set[Variable] = f match {
      case v: Variable => Set(v)
      case Not(f) => collectVariables(f)
      case And(f1, f2) => collectVariables(f1) | collectVariables(f2)
      case Or(f1, f2) => collectVariables(f1) | collectVariables(f2)
    }

    case class DNF(universe: Set[Variable], truthfulAssignments: Set[Set[Variable]])

    def formula2dnf(f: Formula): DNF = {
      val universe = collectVariables(f)
      val possibleWorlds = universe.subsets
      val truthfulAssignments = possibleWorlds.filter(eval(_)(f)).toSet
      DNF(universe, truthfulAssignments)
    }

    val dnf2formula: DNF => Either[Boolean, Formula] = {
      import scala.math.Ordering
      import Ordering.Implicits._
      implicit val literalOrdering: Ordering[Formula] = Ordering.by[Formula, (String, Int)] {
        case Variable(v) => (v.name, 0)
        case Not(Variable(v)) => (v.name, 1)
        case _ => throw new AssertionError
      }
      implicit val clausesOrdering = Ordering[Seq[Formula]]
      (dnf: DNF) => {
        val DNF(universe, truthfulAssignments) = dnf
        if (truthfulAssignments.isEmpty) Left(false)
        else if (truthfulAssignments == universe.subsets.toSet) Left(true)
        else Right {
          assert(truthfulAssignments.forall(!_.isEmpty))
          def clause(trueAssignments: Set[Variable]): Iterable[Formula] = {
            val falseAssignments = (universe &~ trueAssignments).map(Not)
            trueAssignments ++ falseAssignments
          }
          val clauses = truthfulAssignments.map(clause)
          val conjunctions = clauses.map(_.toSeq.sorted(literalOrdering)).toSeq
          val disjunctions = conjunctions.toSeq.sorted(clausesOrdering)
          disjunctions.map(_.reduceLeft(And)).reduceLeft(Or)
        }
      }
    }

  }

}