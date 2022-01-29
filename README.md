# zio-feather

## What is this
zio-feather is a experimental library.  
It is build on top of scala3 and [ZIO](https://github.com/zio/zio).  
ZIO is a excellent functional library.

zio-feather aims at
* simplify dependency management 
  * compile time dependency injection using scala3 context (implicit).
  * ZIO friendly (R port), and partial injection is enabled.
* simplify error modeling and handling
  * model errors as enum or type aliases.
  * automatic stack tracing on mapping errors.

Following specific module patterns in zio-feather, 
you can minimumize boiler codes (duplicated sigunitures, type annotations, etc).
And your program looks like a scripting language (+ type definitions).

## Examples


## License
[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
