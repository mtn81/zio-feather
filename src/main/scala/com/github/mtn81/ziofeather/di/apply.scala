package com.github.mtn81.ziofeather.di

import zio.*

object apply:

  extension [R, E, A](
    f: () => ZIO[R, E, A]
  )
    def on[R1 >: R](injectFn: ZIO[R, E, A] => ZIO[R1, E, A]): 
      () => ZIO[R1, E, A] =
      () => injectFn(f())
  
  extension [R, E, A, P1](
    f: P1 => ZIO[R, E, A]
  )
    def on[R1 >: R](injectFn: ZIO[R, E, A] => ZIO[R1, E, A]): 
      P1 => ZIO[R1, E, A] =
      (p1) => injectFn(f(p1))
  
  extension [R, E, A, P1, P2](
    f: (P1, P2) => ZIO[R, E, A]
  )
    def on[R1 >: R](injectFn: ZIO[R, E, A] => ZIO[R1, E, A]): 
      (P1, P2) => ZIO[R1, E, A] =
      (p1, p2) => injectFn(f(p1, p2))
  
  extension [R, E, A, P1, P2, P3](
    f: (P1, P2, P3) => ZIO[R, E, A]
  )
    def on[R1 >: R](injectFn: ZIO[R, E, A] => ZIO[R1, E, A]): 
      (P1, P2, P3) => ZIO[R1, E, A] =
      (p1, p2, p3) => injectFn(f(p1, p2, p3))
  
  extension [R, E, A, P1, P2, P3, P4](
    f: (P1, P2, P3, P4) => ZIO[R, E, A]
  )
    def on[R1 >: R](injectFn: ZIO[R, E, A] => ZIO[R1, E, A]): 
      (P1, P2, P3, P4) => ZIO[R1, E, A] =
      (p1, p2, p3, p4) => injectFn(f(p1, p2, p3, p4))
  
  extension [R, E, A, P1, P2, P3, P4, P5](
    f: (P1, P2, P3, P4, P5) => ZIO[R, E, A]
  )
    def on[R1 >: R](injectFn: ZIO[R, E, A] => ZIO[R1, E, A]): 
      (P1, P2, P3, P4, P5) => ZIO[R1, E, A] =
      (p1, p2, p3, p4, p5) => injectFn(f(p1, p2, p3, p4, p5))
  
  