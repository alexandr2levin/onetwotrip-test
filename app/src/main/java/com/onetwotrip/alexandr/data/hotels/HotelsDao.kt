package com.onetwotrip.alexandr.data.hotels

import com.onetwotrip.alexandr.data.server.OneTwoTripServer
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory

class HotelsDao(
    private val server: OneTwoTripServer
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getAll(): Single<List<Hotel>> = server.hotels()
        .map { it.hotels }

}