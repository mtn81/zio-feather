package io.github.mtn81.ziofeather.di

import zio.*
import scala.compiletime.*

import io.github.mtn81.ziofeather.tuple.types.*

object inject:
  import types.*

  trait TotalInjection extends PartialInjection:
    type ExternalDeps = EmptyTuple

  object injection extends TotalInjection

  trait PartialInjection:
    type ExternalDeps
    private type ResultDeps = ZEnv & ToHas[ExternalDeps]

    extension [R, E, A](z: ZIO[ResultDeps & Has[R], E, A])

      def inject(r: R)(using
        tag: Tag[R],
        hasTag: Tag[Has[R]]
      ): ZIO[ResultDeps, E, A] =
        z.provideSome[ResultDeps](_ ++ Has(r))

      def inject_(using
        r: R,
        tag: Tag[R],
        hasTag: Tag[Has[R]]
      ): ZIO[ResultDeps, E, A] =
        inject(r)

    extension [R1, R2, E, A](z: ZIO[ResultDeps & Has[R1] & Has[R2], E, A])

      def inject(r1: R1, r2: R2)(using
        tag1: Tag[R1],
        hasTag1: Tag[Has[R1]],
        tag2: Tag[R2],
        hasTag2: Tag[Has[R2]]
      ): ZIO[ResultDeps, E, A] =
        z.provideSome[ResultDeps](_ ++ Has(r1) ++ Has(r2))

      def inject_(using
        r1: R1,
        r2: R2,
        tag1: Tag[R1],
        hasTag1: Tag[Has[R1]],
        tag2: Tag[R2],
        hasTag2: Tag[Has[R2]]
      ): ZIO[ResultDeps, E, A] =
        inject(r1, r2)

    extension [R1, R2, R3, E, A](z: ZIO[ResultDeps & Has[R1] & Has[R2] & Has[R3], E, A])

      def inject(r1: R1, r2: R2, r3: R3)(using
        tag1: Tag[R1],
        hasTag1: Tag[Has[R1]],
        tag2: Tag[R2],
        hasTag2: Tag[Has[R2]],
        tag3: Tag[R3],
        hasTag3: Tag[Has[R3]]
      ): ZIO[ResultDeps, E, A] =
        z.provideSome[ResultDeps](_ ++ Has(r1) ++ Has(r2) ++ Has(r3))

      def inject_(using
        r1: R1,
        r2: R2,
        r3: R3,
        tag1: Tag[R1],
        hasTag1: Tag[Has[R1]],
        tag2: Tag[R2],
        hasTag2: Tag[Has[R2]],
        tag3: Tag[R3],
        hasTag3: Tag[Has[R3]]
      ): ZIO[ResultDeps, E, A] =
        inject(r1, r2, r3)

    extension [R1, R2, R3, R4, E, A](z: ZIO[ResultDeps & Has[R1] & Has[R2] & Has[R3] & Has[R4], E, A])

      def inject(r1: R1, r2: R2, r3: R3, r4: R4)(using
        tag1: Tag[R1],
        hasTag1: Tag[Has[R1]],
        tag2: Tag[R2],
        hasTag2: Tag[Has[R2]],
        tag3: Tag[R3],
        hasTag3: Tag[Has[R3]],
        tag4: Tag[R4],
        hasTag4: Tag[Has[R4]]
      ): ZIO[ResultDeps, E, A] =
        z.provideSome[ResultDeps](_ ++ Has(r1) ++ Has(r2) ++ Has(r3) ++ Has(r4))

      def inject_(using
        r1: R1,
        r2: R2,
        r3: R3,
        r4: R4,
        tag1: Tag[R1],
        hasTag1: Tag[Has[R1]],
        tag2: Tag[R2],
        hasTag2: Tag[Has[R2]],
        tag3: Tag[R3],
        hasTag3: Tag[Has[R3]],
        tag4: Tag[R4],
        hasTag4: Tag[Has[R4]]
      ): ZIO[ResultDeps, E, A] =
        inject(r1, r2, r3, r4)

    extension [R1, R2, R3, R4, R5, E, A](z: ZIO[ResultDeps & Has[R1] & Has[R2] & Has[R3] & Has[R4] & Has[R5], E, A])

      def inject(r1: R1, r2: R2, r3: R3, r4: R4, r5: R5)(using
        tag1: Tag[R1],
        hasTag1: Tag[Has[R1]],
        tag2: Tag[R2],
        hasTag2: Tag[Has[R2]],
        tag3: Tag[R3],
        hasTag3: Tag[Has[R3]],
        tag4: Tag[R4],
        hasTag4: Tag[Has[R4]],
        tag5: Tag[R5],
        hasTag5: Tag[Has[R5]]
      ): ZIO[ResultDeps, E, A] =
        z.provideSome[ResultDeps](_ ++ Has(r1) ++ Has(r2) ++ Has(r3) ++ Has(r4) ++ Has(r5))

      def inject_(using
        r1: R1,
        r2: R2,
        r3: R3,
        r4: R4,
        r5: R5,
        tag1: Tag[R1],
        hasTag1: Tag[Has[R1]],
        tag2: Tag[R2],
        hasTag2: Tag[Has[R2]],
        tag3: Tag[R3],
        hasTag3: Tag[Has[R3]],
        tag4: Tag[R4],
        hasTag4: Tag[Has[R4]],
        tag5: Tag[R5],
        hasTag5: Tag[Has[R5]]
      ): ZIO[ResultDeps, E, A] =
        // TODO　パラメータの順序がこうじゃないとコンパイルされない。謎
        inject(r3, r4, r1, r2, r5)

  end PartialInjection
