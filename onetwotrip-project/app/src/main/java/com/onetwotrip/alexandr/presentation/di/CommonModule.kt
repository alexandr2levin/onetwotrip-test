package com.onetwotrip.alexandr.presentation.di

import com.google.gson.Gson
import com.onetwotrip.alexandr.presentation.AppSchedulers
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class CommonModule {

    @AppScope
    @Provides
    fun appSchedulers() = AppSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())

    @AppScope
    @Provides
    fun gson() = Gson()

    @AppScope
    @Provides
    fun retrofit() = Retrofit.Builder()
        .baseUrl("https://api.myjson.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}