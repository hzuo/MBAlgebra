package hzuo.mbalgebra.nat

import scalaz.Equal

private object Rep1 extends Nat {

  sealed trait T
  private case object Zero extends T
  private case class Succ(n: T) extends T

  val equality = Equal.equalA[T]

  val zero: T = Zero
  val succ: T => T = Succ
  val pred: T => Option[T] = { (n: T) =>
    n match {
      case Zero => None
      case Succ(pred) => Some(pred)
    }
  }

}

private object Rep2 extends Nat {

  type T = BigInt
  val equality = Equal.equalA[T]
  val zero: T = 0
  val succ: T => T = _ + 1
  val pred: T => Option[T] = { (n: T) =>
    if (n == 0) None
    else if (n > 0) Some(n - 1)
    else throw new AssertionError
  }

}