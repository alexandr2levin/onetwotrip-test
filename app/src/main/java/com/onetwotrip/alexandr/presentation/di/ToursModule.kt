package com.onetwotrip.alexandr.presentation.di

import com.onetwotrip.alexandr.data.server.OneTwoTripServer
import com.onetwotrip.alexandr.data.companies.CompaniesDao
import com.onetwotrip.alexandr.data.flights.FlightsDao
import com.onetwotrip.alexandr.data.hotels.HotelsDao
import com.onetwotrip.alexandr.model.tours.ToursManager
import dagger.Module
import dagger.Provides

@Module
class ToursModule {

    @AppScope
    @Provides
    fun companiesDao(server: OneTwoTripServer) = CompaniesDao(server)

    @AppScope
    @Provides
    fun hotelsDao(server: OneTwoTripServer) = HotelsDao(server)

    @AppScope
    @Provides
    fun flightsDao(server: OneTwoTripServer) = FlightsDao(server)

    @AppScope
    @Provides
    fun toursManager(companiesDao: CompaniesDao, hotelsDao: HotelsDao, flightsDao: FlightsDao)
            = ToursManager(companiesDao, hotelsDao, flightsDao)

}