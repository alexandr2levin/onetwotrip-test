package com.onetwotrip.alexandr.data.server

import com.onetwotrip.alexandr.data.companies.Company
import com.onetwotrip.alexandr.data.flights.Flight
import com.onetwotrip.alexandr.data.hotels.Hotel
import io.reactivex.Single
import retrofit2.http.GET

interface OneTwoTripServer {

    @GET("bins/zqxvw")
    fun flights(): Single<FlightsResponse>

    @GET("bins/12q3ws")
    fun hotels(): Single<HotelsResponse>

    @GET("bins/8d024")
    fun companies(): Single<CompaniesResponse>

}