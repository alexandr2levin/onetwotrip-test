package com.onetwotrip.alexandr.presentation.routes.tours.flights

import android.support.v7.widget.RecyclerView
import android.view.View
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.model.tours.Tour
import com.onetwotrip.alexandr.presentation.utils.NumberUtils
import kotlinx.android.synthetic.main.route_tours_flights_flight.view.*
import org.flycraft.tonguetwisterslibrary.common.data.utils.DateTimeFormatters

class FlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var onClick: (() -> Unit)? = null

    private val resources = itemView.resources

    init {
        itemView.setOnClickListener {
            onClick?.invoke()
        }
    }

    fun setData(flightOption: Tour.FlightOption) {
        itemView.company_text_view.text = flightOption.companyName
        itemView.price_text_view.text = resources.getString(
                R.string.route_tours_flights_flight_price,
                NumberUtils.groupSeparate(flightOption.priceWithHotel, ' ')
        )

        val durationHours = flightOption.duration.toHours()
        val durationMinutes = flightOption.duration.minusHours(durationHours).toMinutes()

        itemView.departure_date_text_view.text = resources.getString(R.string.route_tours_flights_flight_departure_hint)
        itemView.departure_text_view.text = DateTimeFormatters.hhMmFormatter.format(flightOption.departure)
        // todo implement smart align logic for all FlightViewHolder-s and dispose of extra chars in duration_text_view
        itemView.duration_text_view.text = resources.getString(
                R.string.route_tours_flights_flight_duration,
                String.format("%02d", durationHours),
                String.format("%02d", durationMinutes)
        )
        itemView.arrival_date_text_view.text = resources.getString(R.string.route_tours_flights_flight_arrival_hint)
        itemView.arrival_text_view.text = DateTimeFormatters.hhMmFormatter.format(flightOption.arrival)
    }

}