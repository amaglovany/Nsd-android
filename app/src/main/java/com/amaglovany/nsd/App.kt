package com.amaglovany.nsd

import android.app.Application
import com.amaglovany.nsd.di.module.AppModule
import toothpick.Toothpick
import toothpick.configuration.Configuration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(if (BuildConfig.DEBUG) {
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction().disableReflection()
        })

        val scope = Toothpick.openScope(this)
        scope.installModules(AppModule(this))
        Toothpick.inject(this, scope)
    }
}