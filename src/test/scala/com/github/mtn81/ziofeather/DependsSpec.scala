package com.github.mtn81.ziofeather

import zio.*
import zio.test.*
import zio.test.Assertion.*

object DependsSpec extends DefaultRunnableSpec {
  def spec =
    suite("dependsOn Spec")(
      testM("dependsOnA, dependsOnFn関数で、ZIOに依存関係を追加できる") {
        trait A

        trait B extends HasFn:
          type Fn = String
          lazy val fn = "function of B"

        assertM(
          for
            a <- dependsOnA[A]
            b <- dependsOnFn[B]
          yield s"success: ${b}"
        )(equalTo("success: function of B"))
          .provide(Has(new A {}) ++ Has(new B {}))
      },
      testM("dependsOn関数で、ZIOに依存関係を追加できる") {

        for
          r1 <- assertM(
                  dependsOn[String] { s =>
                    ZIO.succeed(s)
                  }
                )(equalTo("test1")).provide(Has("test1"))

          r2 <- assertM(
                  dependsOn[String * Int] { (s, i) =>
                    ZIO.succeed((s, i))
                  }
                )(equalTo(("test2", 1))).provide(Has("test2") ++ Has(1))
        yield r1 && r2

      }
    )

}