package com.onetwotrip.alexandr.model.tours

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchParams(
    val companiesIds: List<Int>
) : Parcelable