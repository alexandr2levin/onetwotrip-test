package com.onetwotrip.alexandr.presentation

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen
import com.onetwotrip.alexandr.presentation.di.AppComponent
import com.onetwotrip.alexandr.presentation.di.DaggerAppComponent

class OneTwoTripApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        ComponentsHolder.addDefault(
            componentType = AppComponent::class,
            component = DaggerAppComponent.builder().build()
        )
    }

}