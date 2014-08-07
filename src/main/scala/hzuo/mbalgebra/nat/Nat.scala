package hzuo.mbalgebra.nat

trait Nat {

  type T

  implicit def equality: scalaz.Equal[T]

  def zero: T
  def succ: T => T

  // necessary at the programmatic level
  def pred: T => Option[T]

  final def compute[X](onzero: => X, onsucc: T => X)(n: T): X = {
    pred(n) match {
      case None => onzero
      case Some(pred) => onsucc(pred)
    }
  }

}