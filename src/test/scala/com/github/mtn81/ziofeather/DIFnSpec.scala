package io.github.mtn81.ziofeather

import zio.*
import zio.test.*
import zio.test.Assertion.*

object DISpec extends ZIOSpecDefault {

  object Fn2:
    given live: Fn2 with {}

  trait Fn2 extends HasFn:
    type Fn = () => XZIO[Any, Nothing, String]
    def fn =
      () => ZIO.succeed("Fn2")

  object Fn3:
    given live: Fn3 with {}

  trait Fn3 extends HasFn:
    type Fn = () => XZIO[Any, Nothing, String]
    def fn =
      () => ZIO.succeed("Fn3")

  def spec =
    suite("DIFn Spec")(
      test("依存関係の解決をして実行できる(１つの依存関係)") {

        trait Fn1 extends DIFn:
          type Deps    = Fn2
          type Impl[R] = () => XZIO[R, Nothing, String]
          def impl =
            () =>
              dependsOn_ { fn2 =>
                fn2.fn().map(s => s"Fn1>$s")
              }

        given fn1: Fn1 with
          def fn = impl on inject_

        assertM(fn1.fn())(equalTo("Fn1>Fn2"))

      },
      test("依存関係の解決をして実行できる(複数の依存関係)") {

        trait Fn1 extends DIFn:
          type Deps    = Fn2 * Fn3
          type Impl[R] = () => XZIO[R, Nothing, String]

          def impl =
            () =>
              dependsOn_ { (fn2, fn3) =>
                for
                  _  <- Console.printLine("test").orDie
                  s2 <- fn2.fn()
                  s3 <- fn3.fn()
                yield s"Fn1>$s2>$s3"
              }

        given fn1: Fn1 with
          def fn = impl on inject_

        assertM(fn1.fn())(equalTo("Fn1>Fn2>Fn3"))

      }
    ) + suite("PartialDIFn Spec")(
      test("部分的に依存関係の解決をして実行できる") {

        trait Fn1 extends PartialDIFn:
          type Deps    = Fn2
          type ExtDeps = Fn3
          type Impl[R] = () => XZIO[R, Nothing, String]

          def impl =
            () =>
              dependsOn_ { (fn2, fn3) =>
                for
                  _  <- Console.printLine("test").orDie
                  s2 <- fn2.fn()
                  s3 <- fn3.fn()
                yield s"Fn1>$s2>$s3"
              }

        given fn1: Fn1 with
          def fn = impl on inject_

        assertM(
          fn1.fn().provideEnvironment(ZEnvironment(new Fn3 {}))
        )(equalTo("Fn1>Fn2>Fn3"))

      }
    ) + suite("ExternalDIFn Spec")(
      test("依存関係の解決はすべて呼び出し元にして実行できる") {

        trait Fn1 extends ExternalDIFn:
          type ExtDeps = Fn2 * Fn3
          type Impl[R] = () => XZIO[R, Nothing, String]

          def fn =
            () =>
              dependsOn_ { (fn2, fn3) =>
                for
                  _  <- Console.printLine("test").orDie
                  s2 <- fn2.fn()
                  s3 <- fn3.fn()
                yield s"Fn1>$s2>$s3"
              }

        given fn1: Fn1 with {}

        trait Fn4 extends ExternalDIFn:
          type ExtDeps = Fn2
          type Impl[R] = () => XZIO[R, Nothing, String]

          def fn =
            () =>
              dependsOn_ { (fn2) =>
                for s2 <- fn2.fn()
                yield s"Fn4>$s2"
              }

        given fn4: Fn4 with {}

        assertM(
          fn1.fn().provideEnvironment(ZEnvironment(new Fn2 {}, new Fn3 {}))
        )(equalTo("Fn1>Fn2>Fn3"))

      }
    )

}
