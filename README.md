# zio-feather

## What is this
zio-feather is a experimental library.  
It is build on top of scala3 and [ZIO](https://github.com/zio/zio).  

Mixing ZIO and scala3 capability, zio-feather wraps ZIO and it provides simpler usage for ZIO's excellent functionalities.
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


## License
[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
