package com.github.mtn81.ziofeather.di

import zio.*
import scala.compiletime.*

import com.github.mtn81.ziofeather.tuple.types.*

object depends:
  import types.*

  transparent inline def dependsOn[D] =
    inline erasedValue[D] match
      case _: Tuple1[d] => DependsOnOne[d]()
      case _: (h *: t)  => DependsOnTup[h *: t]()
      case _            => DependsOnOne[D]()

  def dependsOnA[R: Tag]: URIO[Has[R], R] =
    ZIO.environment[Has[R]].map(_.get)

  class DependsOnTup[D <: Tuple]:

    inline def apply[R, E, A](f: D => ZIO[R, E, A]): ZIO[R & ToHas[D], E, A] =
      for
        deps   <- depsValue[D]
        result <- f(deps)
      yield result

    // Context function ではTupleは引数展開されないようなので、自前で行う
    transparent inline def context =
      inline erasedValue[D] match
        case _: (d1, d2)         => CtxApply2[d1, d2]
        case _: (d1, d2, d3)     => CtxApply3[d1, d2, d3]
        case _: (d1, d2, d3, d4) => CtxApply4[d1, d2, d3, d4]

    class CtxApply2[D1, D2]:
      private type Tup = (D1, D2)
      inline def apply[R, E, A](f: (D1, D2) ?=> ZIO[R, E, A]): ZIO[R & ToHas[Tup], E, A] =
        for
          deps   <- depsValue[Tup]
          result <- f(using deps(0), deps(1))
        yield result

    class CtxApply3[D1, D2, D3]:
      private type Tup = (D1, D2, D3)
      inline def apply[R, E, A](f: (D1, D2, D3) ?=> ZIO[R, E, A]): ZIO[R & ToHas[Tup], E, A] =
        for
          deps   <- depsValue[Tup]
          result <- f(using deps(0), deps(1), deps(2))
        yield result

    class CtxApply4[D1, D2, D3, D4]:
      private type Tup = (D1, D2, D3, D4)
      inline def apply[R, E, A](f: (D1, D2, D3, D4) ?=> ZIO[R, E, A]): ZIO[R & ToHas[Tup], E, A] =
        for
          deps   <- depsValue[Tup]
          result <- f(using deps(0), deps(1), deps(2), deps(3))
        yield result

    inline def depsValue[D2 <: Tuple]: URIO[ToHas[D2], D2] =
      (inline erasedValue[D2] match {
        case _: EmptyTuple =>
          ZIO.succeed(EmptyTuple)

        case _: (h *: t) =>
          for
            head <- dependsOnA[h]
            tail <- depsValue[t]
          yield head *: tail

      }).asInstanceOf[URIO[ToHas[D2], D2]]

  class DependsOnOne[D]:
    inline def apply[R, E, A](f: D => ZIO[R, E, A]): ZIO[R & Has[D], E, A] =
      for
        deps   <- dependsOnA[D]
        result <- f(deps)
      yield result

    inline def context[R, E, A](f: D ?=> ZIO[R, E, A]): ZIO[R & Has[D], E, A] =
      apply(d1 => f(using d1))
