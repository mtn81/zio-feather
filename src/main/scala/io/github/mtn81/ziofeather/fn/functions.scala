package io.github.mtn81.ziofeather.fn

import zio.*

object functions:
  import templates.*

  transparent inline def dependsOnFn[R <: HasFn] =
    ZIO.environment[R].map(_.get[R].fn)
