package com.onetwotrip.alexandr.data.flights

import com.onetwotrip.alexandr.data.server.FlightsResponse
import com.onetwotrip.alexandr.data.server.OneTwoTripServer
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import java.util.*

class FlightsDao(
    val server: OneTwoTripServer
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getAll() = server.flights().map { it.flights }

}