package io.github.mtn81.ziofeather.error

import zio.*

object types:

  case class AppErr[+A] private (
    value: A,
    message: String,
    cause: Option[Throwable]
  ) extends Exception(message, cause.getOrElse(null)):

    def map[B](f: A => B): AppErr[B] =
      val b = f(value)
      AppErr(b, s"[Mapped error: $b] < caused by ${this.message}", Some(this))

  object AppErr:
    inline def apply[E](e: E): AppErr[E] =
      AppErr(e, s"[Root error: $e]", None)
    inline def apply[E](e: E, cause: Throwable): AppErr[E] =
      AppErr(e, s"[Root error: $e]", Some(cause))
