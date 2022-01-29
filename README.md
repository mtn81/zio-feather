# zio-feather

## What is this
zio-feather is a experimental library.  
It is build on top of scala3 and [ZIO](https://github.com/zio/zio).  

Mixing ZIO and scala3 capability, zio-feather wraps ZIO and it provides simpler usage for ZIO's excellent functionalities, especially

* simplify dependency management 
  * compile time dependency injection using scala3's context (implicit).
  * ZIO friendly (R port), and partial injection is enabled.
  * DSL for DI
* simplify error modeling and handling
  * model errors as enum or type aliases for each functions, so you can trace errors by types. (no global error list).
  * minimumize error integration codes.
  * automatic stack tracing on mapping errors.

Following specific module patterns in zio-feather, you can minimumize boiler codes (duplicated sigunitures, type annotations, etc).
And your programs may bocome simple and looks like a scripting language (+ type definitions).

## Examples

### Module Pattern
* For service functions, put one public function in one trait. (application service, domain service, infrastracture service...)
* Leaf function
  * Implement fn (fn is a public port).
  * Put live instance (implimentation instance) in companion.

```
// Leaf Function (no dependency)

import com.github.mtn81.ziofeather.*

object DoHoge:
  given live: DoHoge with {}
  
trait DoHoge extends HasFn
  type Fn = ...
  lazy val fn = ...
```

* Parent Function 
  * Define dependency types in ```Deps``` type.
  * Implement ```lazy val impl``` (using ```dependsOn_```)
  * Inject dependencies in live instance creation by defining fn, using ```inject_```.
```
// Parent Function (has dependency)

import com.github.mtn81.ziofeather.*

object DoBar:
  given live: DoBar with
    lazy val fn = impl on inject_
  
trait DoBar extends DIFn:
  type Deps = DoHoge * DoBaz  // depencency
  type Impl[R] = (A) => XZIO[R, Err, B]
  lazy val impl =
    a =>
    dependsOn_ { (doHoge, doBaz) =>
      ...
    }

```
* You can easily create mock function for testing by implement mock fn.
* You can controll injected instance by setting given scope properly. (ex. live instances not in companion.)

### Error Handling
* Put error types in companion. (enum or type alias)
* Mixin ```WithErr``` and use ```.err_``` for error mapping

```
(type alias)

object DoHoge extends HasErr:
  type Err = DoA | DoB
  ...

trait DoHoge extends DIFn, WithErr(DoHoge):
  import DoHoge.*
  
  type Deps = DoA * DoB
  type Impl[R] = () => XZIO[R, Err, Unit]
  ...
  lazy val impl =
    ...
    for
      a <- doA.fn().err_  // err_ is unnessesarry if you do not need stack tracing.
      b <- doB.fn().err_  // err_ is unnessesarry if you do not need stack tracing.
    yield
      ()
```

```
(enums)

object DoHoge extends HasErr:
  enum Err:
    case Failed(cause: DoA | DoB)
    case MyErr
  ...

trait DoHoge extends DIFn, WithErr(DoHoge):
  import DoHoge.*
  
  type Deps = DoA * DoB
  type Impl[R] = () => XZIO[R, Err, Unit]
  ...
  lazy val impl =
    ...
    for
      a <- doA.fn().err_ 
      b <- doB.fn().err_ 
      _ <- Err.MyErr.err
    yield
      ()
```

## License
[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
