package io.github.mtn81.ziofeather.di

import zio.*
import scala.compiletime.*

import io.github.mtn81.ziofeather.tuple.types.*
import io.github.mtn81.ziofeather.di.depends.*

object inject:
  import types.*

  trait TotalInjection extends PartialInjection:
    type ExternalDeps = EmptyTuple

  object injection extends TotalInjection

  trait PartialInjection:
    type Deps
    type ExternalDeps
    type TotalDeps = Deps * ExternalDeps
    type ResultEnv = ZEnv & ToEnv[ExternalDeps]

    protected transparent inline def dependsOn_ = dependsOn[TotalDeps]

    inline def inject_[E, A]: ZIO[ResultEnv & ToEnv[Deps], E, A] => ZIO[ResultEnv, E, A] =
      (z: ZIO[ResultEnv & ToEnv[Deps], E, A]) =>
        z.provideSomeEnvironment[ResultEnv](
          _ ++ (env[ToTup[Deps]](ZEnvironment.empty)).asInstanceOf[ZEnvironment[ToEnv[Deps]]]
        )

    inline def env[R <: Tuple](value: ZEnvironment[?]): ZEnvironment[?] =
      inline erasedValue[R] match
        case _: (h *: t) =>
          env[t](value.add(summonInline[h]))
        case _ =>
          value

  end PartialInjection
