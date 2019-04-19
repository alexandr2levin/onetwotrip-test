package com.onetwotrip.alexandr.data.companies

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Company(
    val id: Int,
    val name: String
) : Parcelable