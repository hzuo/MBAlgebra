# MBAlgebra

A programmatic manifestation of the mathematical concepts presented in Mac Lane & Birkhoff 1967.

Beauty is emphasized.

## Excerpts

### Peano Naturals

```scala
scala> import hzuo.mbalgebra.nat._
import hzuo.mbalgebra.nat._

scala> val specs = new Specs(rep)
specs: hzuo.mbalgebra.nat.Specs = Prop

scala> specs.check
+ Nat.Peano1: OK, passed 100 tests.
+ Nat.Peano2: OK, passed 100 tests.
+ Nat.Peano3: OK, passed 100 tests.
+ Nat.pred: OK, passed 100 tests.
+ Nat.add1=add2: OK, passed 100 tests.
+ Nat.multiply1=multiply2: OK, passed 100 tests.
```

### Propositional Logic

```scala
scala> import hzuo.mbalgebra.util.PropLogic._
import hzuo.mbalgebra.util.PropLogic._

scala> val Some(lhs) = ui.from("x * y + !x * z + y * z")
lhs: hzuo.mbalgebra.util.PropLogic.Formula = Or(Or(And(Variable('x),Variable('y)),And(Not(Variable('x)),Variable('z))),And(Variable('y),Variable('z)))

scala> val Some(rhs) = ui.from("x * y + !x * z")
rhs: hzuo.mbalgebra.util.PropLogic.Formula = Or(And(Variable('x),Variable('y)),And(Not(Variable('x)),Variable('z)))

scala> ui.to(dnf(lhs).right.get)
res0: scalaz.Scalaz.Id[String] = x * y * z + x * y * !z + !x * y * z + !x * !y * z

scala> ui.to(dnf(rhs).right.get)
res1: scalaz.Scalaz.Id[String] = x * y * z + x * y * !z + !x * y * z + !x * !y * z

scala> import scalaz._, Scalaz._
import scalaz._
import Scalaz._

scala> lhs === rhs
res2: Boolean = true
```
