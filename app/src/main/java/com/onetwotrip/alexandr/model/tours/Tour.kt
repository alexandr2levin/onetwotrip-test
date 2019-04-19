package com.onetwotrip.alexandr.model.tours

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime

@Parcelize
data class Tour(
        val hotelId: Int,
        val hotelName: String,
        val hotelPrice: Int,
        val flightOptions: List<FlightOption>
) : Parcelable {
    @Parcelize
    data class FlightOption(
            val flightId: Int,
            val companyName: String,
            val departure: LocalTime,
            val arrival: LocalTime,
            val duration: Duration,
            val priceWithHotel: Int
    ) : Parcelable
}