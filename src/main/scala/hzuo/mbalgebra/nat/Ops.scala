package hzuo.mbalgebra.nat

class Ops(val nat: Nat) {

  def iterate[X](f: X => X): nat.T => X => X = {
    nat.compute[X => X](identity[X], pred => f compose iterate(f)(pred))
  }

  def add1(m: nat.T, n: nat.T): nat.T = {
    iterate(nat.succ)(n)(m)
  }

  def multiply1(m: nat.T, n: nat.T): nat.T = {
    val msucc: nat.T => nat.T = iterate(nat.succ)(m)
    iterate(msucc)(n)(nat.zero)
  }

  def add2(m: nat.T, n: nat.T): nat.T = {
    nat.compute(m, pred => nat.succ(add1(m, pred)))(n)
  }

  def multiply2(m: nat.T, n: nat.T): nat.T = {
    nat.compute(nat.zero, pred => add(multiply1(m, pred), m))(n)
  }

  val add = add1 _
  val multiply = multiply1 _

  // TODO:
  // can we transform add into add1?
  // can we transform multiply into multiply1?
  // e.g. http://en.wikipedia.org/wiki/Bird%E2%80%93Meertens_Formalism

}