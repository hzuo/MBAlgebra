package hzuo.mbalgebra.nat

import scalaz._
import Scalaz._

import org.scalacheck._, Gen._, Arbitrary._, Prop._

class Specs(nat0: Nat) extends Properties("Nat") {

  val ops = new Ops(nat0)
  import ops._

  implicit val arbNat: Arbitrary[nat.T] = {
    // http://en.wikipedia.org/wiki/Recursive_data_type#Theory
    def listToNat[X](xs: List[X]): nat.T = xs.foldLeft(nat.zero)((n, x) => nat.succ(n))
    def genNat = arbitrary[List[Unit]].map(listToNat)
    Arbitrary(genNat)
  }

  property("Peano1") = forAll { (n: nat.T) =>
    import nat._
    nat.succ(n) =/= nat.zero
  }

  property("Peano2") = forAll { (a: nat.T, b: nat.T) =>
    import nat._
    (a =/= b) ==> (nat.succ(a) =/= nat.succ(b))
  }

  property("Peano3") = forAll { (ns: Set[nat.T]) =>
    // we cannot actually check the induction axiom in finite time
    // so this is our best attempt
    ns.contains(nat.zero) ==> ns.exists(n => !ns.contains(nat.succ(n)))
    // TODO: is there a finite set that satisfies Peano's axioms?
  }

  property("pred") = forAll { (n: nat.T) =>
    import nat._
    pred(succ(n)) match {
      case None => n === nat.zero
      case Some(pred) => n === pred
    }
  }

  property("add1=add2") = forAll { (a: nat.T, b: nat.T) =>
    add1(a, b) == add2(a, b)
  }

  property("multiply1=multiply2") = forAll { (a: nat.T, b: nat.T) =>
    multiply1(a, b) == multiply2(a, b)
  }

}