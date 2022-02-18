# Dependency Injection

* For service functions, put one public function in one trait. (application service, domain service, infrastracture service...)
* Leaf function
  * Implement fn (fn is a public port).
  * Put live instance (implimentation instance) in companion.

```scala
// Leaf Function (no dependency)

import io.github.mtn81.ziofeather.*

object DoHoge:
  given live: DoHoge with {}
  
trait DoHoge extends HasFn
  type Fn = ...
  def fn = ...
```

* Parent Function 
  * Define dependency types in ```Deps``` type, and function type in ```Impl[R]``` type
  * Implement ```def impl``` (using ```dependsOn_```)
  * Inject dependencies in live instance creation by defining fn, using ```inject_```.

```scala
// Parent Function (has dependency)

import io.github.mtn81.ziofeather.*

object DoBar:
  given live: DoBar with
    def fn = impl on inject_
  
trait DoBar extends DIFn:
  type Deps = DoHoge * DoBaz  // depencency
  type Impl[R] = (A) => XZIO[R, Err, B]
  def impl =
    a =>
    dependsOn_ { (doHoge, doBaz) =>
      ...
    }

```
* You can easily create mock function for testing by implement mock fn.
* You can controll injected instance by setting given scope properly. (ex. live instances not in companion.)
