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

    inline def inject[E, A]: ZIO[ResultEnv & ToEnv[Deps], E, A] => ZIO[ResultEnv, E, A] =
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

    extension [E, A](
      f: () => ZIO[ResultEnv & ToEnv[Deps], E, A]
    )
      inline def injected: () => ZIO[ResultEnv, E, A] =
        () => inject(f())

    extension [E, A, P1](
      f: P1 => ZIO[ResultEnv & ToEnv[Deps], E, A]
    )
      inline def injected: P1 => ZIO[ResultEnv, E, A] =
        p1 => inject(f(p1))

    extension [E, A, P1, P2](
      f: (P1, P2) => ZIO[ResultEnv & ToEnv[Deps], E, A]
    )
      inline def injected: (P1, P2) => ZIO[ResultEnv, E, A] =
        (p1, p2) => inject(f(p1, p2))

    extension [E, A, P1, P2, P3](
      f: (P1, P2, P3) => ZIO[ResultEnv & ToEnv[Deps], E, A]
    )
      inline def injected: (P1, P2, P3) => ZIO[ResultEnv, E, A] =
        (p1, p2, p3) => inject(f(p1, p2, p3))
    extension [E, A, P1, P2, P3, P4](
      f: (P1, P2, P3, P4) => ZIO[ResultEnv & ToEnv[Deps], E, A]
    )
      inline def injected: (P1, P2, P3, P4) => ZIO[ResultEnv, E, A] =
        (p1, p2, p3, p4) => inject(f(p1, p2, p3, p4))
    extension [E, A, P1, P2, P3, P4, P5](
      f: (P1, P2, P3, P4, P5) => ZIO[ResultEnv & ToEnv[Deps], E, A]
    )
      inline def injected: (P1, P2, P3, P4, P5) => ZIO[ResultEnv, E, A] =
        (p1, p2, p3, p4, p5) => inject(f(p1, p2, p3, p4, p5))

  end PartialInjection
