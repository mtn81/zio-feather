package com.github.mtn81.ziofeather.tuple

object types:

  type ToTup[A] <: Tuple = A match
    case Tuple => Tuple.Concat[A, EmptyTuple]
    case _     => Tuple1[A]

  /**
   * ・ A * B => (A, B) ・ A * B * C => (A, B, C) ・ (A * B) * (C * D) => (A, B, C,
   * D)
   */
  type *[A, B] <: Tuple = (A, B) match
    case (Tuple, Tuple) => Tuple.Concat[A, B]
    case (_, Tuple)     => A *: B
    case (Tuple, _)     => Tuple.Concat[A, Tuple1[B]]
    case _              => (A, B)
