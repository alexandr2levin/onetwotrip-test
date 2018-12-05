package com.onetwotrip.alexandr.presentation.routes.tours.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.model.tours.Tour
import com.onetwotrip.alexandr.presentation.utils.NumberUtils
import kotlinx.android.synthetic.main.route_main_tour.view.*

class TourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val resources = itemView.resources

    private var onFlightChanged: (() -> Unit)? = null

    init {
        itemView.setOnClickListener {

        }
    }

    fun setData(tour: Tour) {
        val flightOptionsCount = tour.flightOptions.size
        val cheapestFlightOption = tour.flightOptions.first()

        itemView.hotel_name_text_view.text = tour.hotelName

        val priceText = NumberUtils.groupSeparate(
            tour.hotelPrice + tour.flightOptions.first().price,
            ' '
        )
        if(flightOptionsCount == 1) {
            itemView.flight_info_text_view.text = cheapestFlightOption.companyName
            itemView.price_text_view.text = resources.getString(R.string.route_main_header_single_price, priceText)
        } else {
            itemView.flight_info_text_view.text = resources.getQuantityString(
                R.plurals.route_main_tour,
                flightOptionsCount,
                flightOptionsCount
            )
            itemView.price_text_view.text = resources.getString(R.string.route_main_header_min_price, priceText)
        }
    }

}