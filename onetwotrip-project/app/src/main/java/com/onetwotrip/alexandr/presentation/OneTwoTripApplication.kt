package com.onetwotrip.alexandr.presentation

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class OneTwoTripApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}