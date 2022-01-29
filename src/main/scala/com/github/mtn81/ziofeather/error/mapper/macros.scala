package com.github.mtn81.ziofeather.error.mapper

import scala.quoted.*

object Macros:
  inline def companionApply[S, T]: (S => T) =
    ${ companionApplyImpl[S, T] }

  private def companionApplyImpl[S: Type, T: Type](using Quotes): Expr[S => T] =
    new Macros[S, T].companionApplyImpl

class Macros[S: Type, T: Type](using Quotes):
  import quotes.reflect.*

  def companionApplyImpl: Expr[S => T] =

    val sym = TypeRepr.of[T].typeSymbol.companionClass

    to { (x: Expr[S]) =>
      Apply(Select.unique(This(sym), "apply"), List(x.asTerm))
        .asExprOf[T]
    }

  def to[T: Type, R: Type](f: Expr[T] => Expr[R]): Expr[T => R] =
    '{ (x: T) => ${ f('x) } }
