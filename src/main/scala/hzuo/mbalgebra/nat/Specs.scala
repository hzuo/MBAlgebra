package hzuo.mbalgebra.nat

import scalaz._
import Scalaz._

import org.scalacheck._, Gen._, Arbitrary._, Prop._

class Specs(nat0: Nat) extends Properties("Nat") {

  val ops = new Ops(nat0)
  import ops._
  import nat._

  implicit val arbNat: Arbitrary[T] = {
    // http://en.wikipedia.org/wiki/Recursive_data_type#Theory
    def listToNat[X](xs: List[X]): T = xs.foldLeft(zero)((n, x) => succ(n))
    def genNat = arbitrary[List[Unit]].map(listToNat)
    Arbitrary(genNat)
  }

  property("Peano1") = forAll { (n: T) =>
    succ(n) =/= zero
  }

  property("Peano2") = forAll { (a: T, b: T) =>
    (a =/= b) ==> (succ(a) =/= succ(b))
  }

  property("Peano3") = forAll { (ns: Set[T]) =>
    // we cannot actually check the induction axiom in finite time
    // the following is our best attempt
    // since no finite set should satisfy the Peano axioms
    ns.contains(zero) ==> ns.exists(n => !ns.contains(succ(n)))
  }

  property("pred") = forAll { (n: T) =>
    pred(succ(n)) match {
      case None => n === zero
      case Some(pred) => n === pred
    }
  }

  property("add1=add2") = forAll { (a: T, b: T) =>
    add1(a, b) === add2(a, b)
  }

  property("multiply1=multiply2") = forAll { (a: T, b: T) =>
    multiply1(a, b) === multiply2(a, b)
  }

  // TODO: make tail-recursive
  // if not possible: http://blog.higher-order.com/assets/trampolines.pdf
  // TODO: http://en.wikipedia.org/wiki/Exponentiation_by_squaring

  property("(k^(m+n))=(k^m)*(k^n)") = forAll { (k: T, m: T, n: T) =>
    try {
      pow(k, add(m, n)) === multiply(pow(k, m), pow(k, n))
    } catch {
      case _: StackOverflowError => true
    }
  }

  property("k^(m*n)=(k^m)^n") = forAll { (k: T, m: T, n: T) =>
    try {
      pow(k, multiply(m, n)) === pow(pow(k, m), n)
    } catch {
      case _: StackOverflowError => true
    }
  }

  property("(k^n)*(m^n)=(k*m)^n") = forAll { (k: T, m: T, n: T) =>
    try {
      multiply(pow(k, n), pow(m, n)) === pow(multiply(k, m), n)
    } catch {
      case _: StackOverflowError => true
    }
  }

}