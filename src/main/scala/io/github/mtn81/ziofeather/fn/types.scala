package io.github.mtn81.ziofeather.fn

import zio.*

import io.github.mtn81.ziofeather.error.types.*

object types:
  type XZIO[-R, +E, +A] = ZIO[R, AppErr[E], A]
  type XIO[+E, +A]      = IO[AppErr[E], A]
