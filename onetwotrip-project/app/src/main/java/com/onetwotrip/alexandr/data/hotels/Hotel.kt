package com.onetwotrip.alexandr.data.hotels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Hotel(
    val id: Int,
    var flights: List<Int>,
    var name: String,
    var price: Int
) : Parcelable