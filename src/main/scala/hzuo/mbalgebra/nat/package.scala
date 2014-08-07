package hzuo.mbalgebra

package object nat {

  val rep1: Nat = Rep1
  val ops1: Ops = new Ops(rep1)

  val rep2: Nat = Rep2
  val ops2: Ops = new Ops(rep2)

  val rep: Nat = rep1
  val ops: Ops = ops1

}