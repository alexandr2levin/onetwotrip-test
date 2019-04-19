package com.onetwotrip.alexandr.data.server

import com.onetwotrip.alexandr.data.flights.Flight

data class FlightsResponse(
    val flights: List<Flight>
)