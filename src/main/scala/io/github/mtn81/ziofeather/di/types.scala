package io.github.mtn81.ziofeather.di

import zio.*

object types:

  type Deps0    = EmptyTuple
  type Deps1[A] = Tuple1[A]

  type ToEnv[D] = D match
    case h *: t     => h & ToEnv[t]
    case EmptyTuple => Any
    case _          => D
