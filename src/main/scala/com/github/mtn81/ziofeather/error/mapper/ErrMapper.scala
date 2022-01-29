package com.github.mtn81.ziofeather.error.mapper

import scala.deriving.*
import scala.compiletime.*

/**
 * C => E のエラー情報の変換
 */
sealed trait ErrMapper[C, E] extends (C => E)

object ErrMapper:
  trait Available[C, E] extends ErrMapper[C, E]

  class Unavailable[C, E] extends ErrMapper[C, E]:
    def apply(cause: C): E =
      throw new Exception("Unsupported conversion")

  transparent inline given ErrMapper[C, E]: ErrMapper[C, E] =
    summonFrom {
      case ev: (C <:< E) =>
        new Available[C, E]:
          def apply(cause: C): E =
            cause.asInstanceOf[E]
      case _ =>
        ErrMapperDeriving[C, E](using summonInline[Mirror.Of[E]])
    }

  transparent inline def ErrMapperDeriving[C, E](using m: Mirror.Of[E]): ErrMapper[C, E] =
    inline m match
      case s: Mirror.SumOf[E] =>
        summonApropriate[C, E, m.MirroredElemTypes]

      case p: Mirror.ProductOf[E] =>
        summonForProduct[C, E](p)

  transparent inline def summonApropriate[C, E, R]: ErrMapper[C, E] =
    inline erasedValue[R] match

      case _: (t *: ts) =>
        inline summonInline[ErrMapper[C, t]] match
          case x: Available[C, t] =>
            x.asInstanceOf[ErrMapper[C, E]]
          case _ =>
            summonApropriate[C, E, ts]

      case _: EmptyTuple =>
        error("compile error: ErrMapper is not found ")

  transparent inline def summonForProduct[C, E](p: Mirror.ProductOf[E]): ErrMapper[C, E] =
    inline erasedValue[p.MirroredElemTypes] match
      case _: Tuple1[a] =>
        // ErrMapper[C, E] の C と 変換先の E.X(a) の a に互換性があること
        inline erasedValue[C] match
          case _: a =>
            new Available[C, E] {
              def apply(c: C): E =
                Macros.companionApply[C, E](c)
            }
          case _ =>
            new Unavailable[C, E]
      case _ =>
        new Unavailable[C, E]
