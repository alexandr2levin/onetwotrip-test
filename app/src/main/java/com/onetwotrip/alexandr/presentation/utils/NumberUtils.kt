package com.onetwotrip.alexandr.presentation.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

object NumberUtils {

    fun groupSeparate(number: Int, separator: Char): String {
        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        val symbols = formatter.decimalFormatSymbols

        symbols.groupingSeparator = separator
        formatter.decimalFormatSymbols = symbols

        return formatter.format(number)
    }

}