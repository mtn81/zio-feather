package com.github.mtn81.ziofeather.zio

import zio.*

import com.github.mtn81.ziofeather.error.types.*

object types:
  type XZIO[-R, +E, +A] = ZIO[ZEnv & R, AppErr[E], A]
  type XIO[+E, +A] = ZIO[ZEnv, AppErr[E], A]
  type XUIO[+A] = ZIO[ZEnv, Nothing, A]
  type XURIO[-R, +A] = ZIO[ZEnv & R, Nothing, A]