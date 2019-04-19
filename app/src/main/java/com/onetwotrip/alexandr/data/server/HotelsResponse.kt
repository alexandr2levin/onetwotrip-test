package com.onetwotrip.alexandr.data.server

import com.onetwotrip.alexandr.data.hotels.Hotel

data class HotelsResponse(
    val hotels: List<Hotel>
)