package com.github.mtn81.ziofeather

import zio.test.*
import zio.test.Assertion.*

object TupleTypeSpec extends DefaultRunnableSpec {
  def spec = 
    suite("ToTup Spec")(
      test("生の型からTuple1への解決ができる") {
        type X = ToTup[String]
        val x: X = Tuple1("")
        assert(true)(isTrue)
      },
      test("Tupleはそのまま") {
        type X = ToTup[(Int, String)]
        val x: X = (1, "")
        assert(true)(isTrue)
      }
    ) +
    suite("*[A, B] Spec")(
      test("シングル * シングル => Tuple2") {
        type X = Int * String
        val x: X = (1, "")
        assert(true)(isTrue)
      },
      test("シングル * Tuple => Tupleの結合") {
        type X = Int * (String, Boolean)
        val x: X = (1, "", true)
        assert(true)(isTrue)
      },
      test("Tuple * シングル => Tupleの結合") {
        type X = (String, Boolean) * Int
        val x: X = ("", true, 1)
        assert(true)(isTrue)
      },
      test("Tuple * Tuple => Tupleの結合") {
        type X = (String, Boolean) * (Int, Double)
        val x: X = ("", true, 1, 1.0)
        assert(true)(isTrue)
      },
    ) 
}