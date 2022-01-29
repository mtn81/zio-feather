package com.github.mtn81.ziofeather.fn

import com.github.mtn81.ziofeather.di.types.*
import com.github.mtn81.ziofeather.di.inject.*
import com.github.mtn81.ziofeather.di.depends.*
import com.github.mtn81.ziofeather.tuple.types.*

object templates:

  trait HasFn:
    type Fn
    lazy val fn: Fn
  
  trait DIFn extends HasFn, TotalInjection:
    type Deps
    type Impl[R]
    type Fn = Impl[Any]
    type ImplFn = Impl[ToHas[Deps]]
  
    lazy val fn: Fn
    lazy val impl: ImplFn
  
    protected transparent inline def dependsOn_ = dependsOn[ToTup[Deps]]
  
  
  trait NoDepsDIFn extends DIFn:
    type Deps = Deps0
  
    override lazy val fn = impl
  
  
  trait PartialDIFn extends HasFn, PartialInjection:
    type InternalDeps
    type ExternalDeps
    type Deps = InternalDeps * ExternalDeps
    type Impl[R]
  
    type ImplFn = Impl[ToHas[Deps]]
    type Fn = Impl[ToHas[ExternalDeps]]
  
    lazy val impl: ImplFn
  
    protected transparent inline def dependsOn_ = dependsOn[ToTup[Deps]]
  
  
  trait ExternalDIFn extends HasFn, PartialInjection:
    type Deps 
    type ExternalDeps = Deps
  
    type Impl[R]
    type Fn = Impl[ToHas[Deps]]
  
    protected transparent inline def dependsOn_ = dependsOn[ToTup[Deps]]
  