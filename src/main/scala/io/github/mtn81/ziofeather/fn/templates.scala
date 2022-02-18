package io.github.mtn81.ziofeather.fn

import io.github.mtn81.ziofeather.di.types.*
import io.github.mtn81.ziofeather.di.inject.*
import io.github.mtn81.ziofeather.di.depends.*
import io.github.mtn81.ziofeather.tuple.types.*

object templates:

  trait HasFn:
    type Fn
    def fn: Fn

  trait DIFn extends HasFn, TotalInjection:
    type Deps
    type Impl[R]
    type Fn     = Impl[Any]
    type ImplFn = Impl[ToHas[Deps]]

    def fn: Fn
    def impl: ImplFn

    protected transparent inline def dependsOn_ = dependsOn[ToTup[Deps]]

  trait NoDepsDIFn extends DIFn:
    type Deps = Deps0

    override def fn = impl

  trait PartialDIFn extends HasFn, PartialInjection:
    type InternalDeps
    type ExternalDeps
    type Deps = InternalDeps * ExternalDeps
    type Impl[R]

    type ImplFn = Impl[ToHas[Deps]]
    type Fn     = Impl[ToHas[ExternalDeps]]

    def impl: ImplFn

    protected transparent inline def dependsOn_ = dependsOn[ToTup[Deps]]

  trait ExternalDIFn extends HasFn, PartialInjection:
    type Deps
    type ExternalDeps = Deps

    type Impl[R]
    type Fn = Impl[ToHas[Deps]]

    protected transparent inline def dependsOn_ = dependsOn[ToTup[Deps]]
