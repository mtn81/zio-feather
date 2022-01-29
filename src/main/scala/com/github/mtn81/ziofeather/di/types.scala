package com.github.mtn81.ziofeather.di

import zio.*

object types:

  type Deps0    = EmptyTuple
  type Deps1[A] = Tuple1[A]

  type ToHas[D] = D match
    case h *: t     => Has[h] & ToHas[t]
    case EmptyTuple => Any
    case _          => Has[D]
