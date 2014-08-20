# MBAlgebra

A programmatic manifestation of the mathematical concepts presented in Mac Lane & Birkhoff 1967.

## Excerpts

### Peano Naturals

```scala
scala> import hzuo.mbalgebra.nat._
import hzuo.mbalgebra.nat._

scala> val specs1 = new Specs(rep1)
specs1: hzuo.mbalgebra.nat.Specs = Prop

scala> specs1.check
+ Nat.Peano1: OK, passed 100 tests.
+ Nat.Peano2: OK, passed 100 tests.
+ Nat.Peano3: OK, passed 100 tests.
+ Nat.pred: OK, passed 100 tests.
+ Nat.add1=add2: OK, passed 100 tests.
+ Nat.multiply1=multiply2: OK, passed 100 tests.
+ Nat.(k^(m+n))=(k^m)*(k^n): OK, passed 100 tests.
+ Nat.k^(m*n)=(k^m)^n: OK, passed 100 tests.
+ Nat.(k^n)*(m^n)=(k*m)^n: OK, passed 100 tests.

scala> val specs2 = new Specs(rep2)
specs2: hzuo.mbalgebra.nat.Specs = Prop

scala> specs2.check
+ Nat.Peano1: OK, passed 100 tests.
+ Nat.Peano2: OK, passed 100 tests.
+ Nat.Peano3: OK, passed 100 tests.
+ Nat.pred: OK, passed 100 tests.
+ Nat.add1=add2: OK, passed 100 tests.
+ Nat.multiply1=multiply2: OK, passed 100 tests.
+ Nat.(k^(m+n))=(k^m)*(k^n): OK, passed 100 tests.
+ Nat.k^(m*n)=(k^m)^n: OK, passed 100 tests.
+ Nat.(k^n)*(m^n)=(k*m)^n: OK, passed 100 tests.
```

### Propositional Logic

```scala
scala> import hzuo.mbalgebra.util.PropLogic._, scalaz._, Scalaz._
import hzuo.mbalgebra.util.PropLogic._
import scalaz._
import Scalaz._

scala> val Some(consensusLHS) = ui.from("x * y + !x * z + y * z")
consensusLHS: hzuo.mbalgebra.util.PropLogic.Formula = Or(Or(And(Variable('x),Variable('y)),And(Not(Variable('x)),Variable('z))),And(Variable('y),Variable('z)))

scala> val Some(consensusRHS) = ui.from("x * y + !x * z")
consensusRHS: hzuo.mbalgebra.util.PropLogic.Formula = Or(And(Variable('x),Variable('y)),And(Not(Variable('x)),Variable('z)))

scala> consensusLHS === consensusRHS
res0: Boolean = true

scala> ui.to(dnf(consensusLHS).right.get)
res1: scalaz.Scalaz.Id[String] = x * y * z + x * y * !z + !x * y * z + !x * !y * z

scala> ui.to(dnf(consensusRHS).right.get)
res2: scalaz.Scalaz.Id[String] = x * y * z + x * y * !z + !x * y * z + !x * !y * z
```
