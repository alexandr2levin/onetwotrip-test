package com.onetwotrip.alexandr.presentation.di

import com.onetwotrip.alexandr.presentation.AppSchedulers
import dagger.Component
import javax.inject.Scope

@Retention(AnnotationRetention.RUNTIME)
@Scope()
annotation class AppScope

@AppScope
@Component(
    modules = [
        CommonModule::class,
        ToursModule::class
    ]
)
interface AppComponent {
    fun appSchedulers(): AppSchedulers
}