package com.github.mtn81.ziofeather.fn

object typeHolders:

  trait GivenThis:
    given this.type = this

  trait HasErr extends GivenThis:
    type Err

  trait HasOutErr extends HasErr:
    type Output

  trait HasInOutErr extends HasOutErr:
    type Input

  trait HasIn extends GivenThis:
    type Input
