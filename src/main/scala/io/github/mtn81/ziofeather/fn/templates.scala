package io.github.mtn81.ziofeather.fn

import io.github.mtn81.ziofeather.di.types.*
import io.github.mtn81.ziofeather.di.inject.*
import io.github.mtn81.ziofeather.di.depends.*
import io.github.mtn81.ziofeather.tuple.types.*
import izumi.reflect.macrortti.LightTypeTagRef.Boundaries.Empty

object templates:

  trait HasFn:
    type Fn
    def fn: Fn

  trait DIFn extends HasFn, TotalInjection:
    type Impl[R]
    type Fn     = Impl[Any]
    type ImplFn = Impl[ToEnv[TotalDeps]]

    def fn: Fn
    def impl: ImplFn

  trait NoDepsDIFn extends DIFn:
    type Deps = Deps0

    override def fn = impl

  trait PartialDIFn extends HasFn, PartialInjection:
    type Impl[R]

    type ImplFn = Impl[ToEnv[TotalDeps]]
    type Fn     = Impl[ToEnv[ExtDeps]]

    def impl: ImplFn

  trait ExternalDIFn extends HasFn, PartialInjection:
    type Deps = EmptyTuple

    type Impl[R]
    type Fn = Impl[ToEnv[ExtDeps]]
