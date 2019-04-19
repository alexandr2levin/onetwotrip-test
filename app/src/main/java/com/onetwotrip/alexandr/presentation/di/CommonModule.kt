package com.onetwotrip.alexandr.presentation.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.onetwotrip.alexandr.data.server.OneTwoTripServer
import com.onetwotrip.alexandr.presentation.AppSchedulers
import com.onetwotrip.alexandr.presentation.utils.LocalTimeGsonConverter
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class CommonModule {

    @AppScope
    @Provides
    fun appSchedulers() = AppSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())

    @AppScope
    @Provides
    fun gson() = GsonBuilder()
        .registerTypeAdapter(LocalTime::class.java, LocalTimeGsonConverter)
        .create()

    @AppScope
    @Provides
    fun retrofit(gson: Gson) = Retrofit.Builder()
        .baseUrl("https://api.myjson.com")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @AppScope
    @Provides
    fun oneTwoTripServer(retrofit: Retrofit)
            = retrofit.create(OneTwoTripServer::class.java)

}