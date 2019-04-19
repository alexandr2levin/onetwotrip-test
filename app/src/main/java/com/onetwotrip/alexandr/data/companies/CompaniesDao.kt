package com.onetwotrip.alexandr.data.companies

import com.onetwotrip.alexandr.data.server.OneTwoTripServer
import io.reactivex.Single
import java.util.*

class CompaniesDao(
    private val server: OneTwoTripServer
) {

    fun getAll() = server.companies()
        .map { it.companies }

}