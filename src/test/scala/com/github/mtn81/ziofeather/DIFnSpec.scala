package com.github.mtn81.ziofeather

import zio.*
import zio.test.*
import zio.test.Assertion.*

object DISpec extends DefaultRunnableSpec {

  object Fn2:
    given live: Fn2 with {}

  trait Fn2 extends HasFn:
    type Fn = () => XZIO[Any, Nothing, String]
    lazy val fn =
      () => ZIO.succeed("Fn2")

  object Fn3:
    given live: Fn3 with {}

  trait Fn3 extends HasFn:
    type Fn = () => XZIO[Any, Nothing, String]
    lazy val fn =
      () => ZIO.succeed("Fn3")

  def spec =
    suite("DIFn Spec")(
      testM("依存関係の解決をして実行できる(１つの依存関係)") {

        trait Fn1 extends DIFn:
          type Deps    = Fn2
          type Impl[R] = () => XZIO[R, Nothing, String]

          lazy val impl =
            () =>
              dependsOn_ { fn2 =>
                fn2.fn().map(s => s"Fn1>$s")
              }

        given fn1: Fn1 with
          lazy val fn = impl on inject_

        assertM(fn1.fn())(equalTo("Fn1>Fn2"))

      },
      testM("依存関係の解決をして実行できる(複数の依存関係)") {

        trait Fn1 extends DIFn:
          type Deps    = Fn2 * Fn3
          type Impl[R] = () => XZIO[R, Nothing, String]

          lazy val impl =
            () =>
              dependsOn_ { (fn2, fn3) =>
                for
                  s2 <- fn2.fn()
                  s3 <- fn3.fn()
                yield s"Fn1>$s2>$s3"
              }

        given fn1: Fn1 with
          lazy val fn = impl on inject_

        assertM(fn1.fn())(equalTo("Fn1>Fn2>Fn3"))

      }
    ) +
      suite("PartialDIFn Spec")(
        testM("部分的に依存関係の解決をして実行できる") {

          trait Fn1 extends PartialDIFn:
            type InternalDeps = Fn2
            type ExternalDeps = Fn3
            type Impl[R]      = () => XZIO[R, Nothing, String]

            lazy val impl =
              () =>
                dependsOn_ { (fn2, fn3) =>
                  for
                    s2 <- fn2.fn()
                    s3 <- fn3.fn()
                  yield s"Fn1>$s2>$s3"
                }

          given fn1: Fn1 with
            lazy val fn = impl on inject_

          assertM(
            fn1.fn().provideSome[ZEnv](_ ++ Has(new Fn3 {}))
          )(equalTo("Fn1>Fn2>Fn3"))

        }
      ) +
      suite("ExternalDIFn Spec")(
        testM("依存関係の解決はすべて呼び出し元にして実行できる") {

          trait Fn1 extends ExternalDIFn:
            type Deps    = Fn2 * Fn3
            type Impl[R] = () => XZIO[R, Nothing, String]

            lazy val fn =
              () =>
                dependsOn_ { (fn2, fn3) =>
                  for
                    s2 <- fn2.fn()
                    s3 <- fn3.fn()
                  yield s"Fn1>$s2>$s3"
                }

          given fn1: Fn1 with {}

          assertM(
            fn1.fn().provideSome[ZEnv](_ ++ Has(new Fn2 {}) ++ Has(new Fn3 {}))
          )(equalTo("Fn1>Fn2>Fn3"))

        }
      )

}
