# Error Handling

* Put error types in companion. (enum or type alias)
* Mixin ```WithErr``` and use ```.mapErr_``` for error mapping

```scala
// error as type alias

object DoHoge extends HasErr:
  type Err = DoA | DoB
  ...

trait DoHoge extends DIFn, WithErr(DoHoge):
  import DoHoge.*
  
  type Deps = DoA * DoB
  type Impl[R] = () => XZIO[R, Err, Unit]
  ...
  def impl =
    ...
    for
      a <- doA.fn().mapErr_  // mapErr_ is unnessesarry if you do not need stack tracing.
      b <- doB.fn().mapErr_  // mapErr_ is unnessesarry if you do not need stack tracing.
    yield
      ()
```

```scala
// error as enum

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
  def impl =
    ...
    for
      a <- doA.fn().mapErr_ 
      b <- doB.fn().mapErr_ 
      _ <- Err.MyErr.err
    yield
      ()
```