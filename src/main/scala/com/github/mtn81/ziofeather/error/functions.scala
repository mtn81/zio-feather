package com.github.mtn81.ziofeather.error

import zio.*
import com.github.mtn81.ziofeather.error.mapper.*

object functions:
  import types.*

  extension [R, E, A](z: ZIO[R, AppErr[E], A])
    inline def mapErr[E1](f: E => E1): ZIO[R, AppErr[E1], A] = 
      z.mapError(_.map(f))
    inline def mapErrTo[E1](using errConv: ErrMapper[E, E1]): ZIO[R, AppErr[E1], A] =
      mapErr[E1](errConv.apply)
  
    inline def catchErr[R1 <: R, E1 >: E, A1 >: A](f: PartialFunction[(E, AppErr[E]), ZIO[R1, AppErr[E1], A1]]): ZIO[R1, AppErr[E1], A1] =
      z.catchSome {
        case err if f.isDefinedAt(err.value, err) =>
          f(err.value, err)
      }
    inline def catchErr[R1 <: R, E1 >: E, A1 >: A](f: (E, AppErr[E]) => ZIO[R1, AppErr[E1], A1]): ZIO[R1, AppErr[E1], A1] =
      z.catchAll { err => f(err.value, err) }
  
    inline def recoverErr(f: PartialFunction[(E, AppErr[E]), A]): ZIO[R, AppErr[E], A] =
      z.catchSome {
        case err if f.isDefinedAt(err.value, err) =>
          UIO.succeed(f(err.value, err))
      }
    inline def recoverErr(f: (E, AppErr[E]) => A): ZIO[R, Nothing, A] =
      z.catchAll { err => UIO.succeed(f(err.value, err)) }
    inline def recoverErrAs[E1 <: E](f: PartialFunction[(E, AppErr[E]), A]): ZIO[R, AppErr[E1], A] =
      recoverErr(f).refineErr[E1]
  
    inline def refineErr[E1 <: E]: ZIO[R, AppErr[E1], A] =
      z.refineOrDie {
        case err if { 
          err.value match
            case _: E1 => true
            case _ => false
        } => 
          err.asInstanceOf[AppErr[E1]]
      }
  
  extension [R, A, T <: Throwable](z: ZIO[R, T, A])
    inline def throwableToErr[E](f: T => E): ZIO[R, AppErr[E], A] = 
      z.mapError(e => AppErr(f(e), e))
    inline def throwableTo[E](using errConv: ErrMapper[T, E]): ZIO[R, AppErr[E], A] =
      throwableToErr[E](errConv.apply)
  
  extension [R, A](z: ZIO[R, Nothing, A])
    inline def err[E]: ZIO[R, AppErr[E], A] =
      z
  
  extension [R, E, A](z: ZIO[R, E, A])
    inline def err[E1](value: => E1): ZIO[R, AppErr[E1], A] = 
      z.mapError(_ => AppErr(value))
  
  extension [E](a: E)
    inline def err: ZIO[Any, AppErr[E], Nothing] =
      ZIO.fail(AppErr(a))
  
  def errWhen[E](b: => Boolean)(e: => E): ZIO[Any, AppErr[E], Unit] =
    ZIO.when(b)(e.err)