package com.onetwotrip.alexandr.data.flights

import org.threeten.bp.LocalTime
import java.util.*

data class Flight(
    val id: Int,
    val companyId: Int,
    val departure: LocalTime,
    val arrival: LocalTime,
    val price: Int
)