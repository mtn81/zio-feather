package com.github.mtn81.ziofeather.fn

import zio.*

object functions:
  import templates.*

  transparent inline def dependsOnFn[R <: HasFn: Tag] =
    ZIO.environment[Has[R]].map(_.get[R].fn)
