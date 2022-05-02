package io.github.mtn81.ziofeather.fn

import zio.*

import io.github.mtn81.ziofeather.error.types.*
import io.github.mtn81.ziofeather.error.functions.*
import io.github.mtn81.ziofeather.error.mapper.*

object errExtensions:
  import typeHolders.*
  import templates.*
  import types.*

  trait WithErr[T <: HasErr](using val types: T):
    import types.*

    extension [R, E, A](z: ZIO[R, AppErr[E], A])
      inline def mapErr_(using errConv: ErrMapper[E, Err]): ZIO[R, AppErr[Err], A] =
        z.mapErrTo[Err]

    extension [R, E, A](z: ZIO[R, Nothing, A])
      inline def err_ : ZIO[R, AppErr[Err], A] =
        z

    extension [R, A, T <: Throwable](z: ZIO[R, T, A])
      inline def terr_(using errConv: ErrMapper[T, Err]): ZIO[R, AppErr[Err], A] =
        z.throwableTo[Err]

    extension [R, E, A](z: ZIO[R, E, A])
      inline def err_(using errConv: ErrMapper[E, Err]): ZIO[R, AppErr[Err], A] =
        z.errTo[Err]

  trait SimpleDIFn extends DIFn:
    type Type <: HasInOutErr
    val types: Type

    import types.*

    type Impl[R] = Input => XZIO[R, Err, Output]

  object SimpleDIFn:
    trait Aux[T <: HasInOutErr](using override val types: T) extends SimpleDIFn, WithErr[T]:

      override type Type = T
