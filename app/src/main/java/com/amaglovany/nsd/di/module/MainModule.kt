package com.amaglovany.nsd.di.module

import com.amaglovany.nsd.repository.NsdInfoRepository
import toothpick.config.Module

class MainModule : Module() {
    init {
        bind(NsdInfoRepository::class.java).singletonInScope()
    }
}