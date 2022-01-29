package com.github.mtn81.ziofeather.fn

import zio.*

import com.github.mtn81.ziofeather.zio.*
import com.github.mtn81.ziofeather.error.types.*
import com.github.mtn81.ziofeather.error.functions.*
import com.github.mtn81.ziofeather.error.mapper.*

object errExtensions:
  import typeHolders.*
  import templates.*

  trait WithErr[T <: HasErr](using val types: T):
    import types.*
  
    extension [R, E, A](z: ZIO[R, AppErr[E], A])
      inline def err_(using errConv: ErrMapper[E, Err]): ZIO[R, AppErr[Err], A] =
        z.mapErrTo[Err]
  
    extension [R, E, A](z: ZIO[R, Nothing, A])
      inline def err_ : ZIO[R, AppErr[Err], A] =
        z
  
    extension [R, A, T <: Throwable](z: ZIO[R, T, A])
      inline def terr_(using errConv: ErrMapper[T, Err]) : ZIO[R, AppErr[Err], A] =
        z.throwableTo[Err]
  
  
  trait SimpleDIFn[T <: HasInOutErr](using override val types: T)
    extends DIFn, WithErr[T]:
    import types.*
  
    type Impl[R] = Input => XZIO[R, Err, Output]
  
  
  