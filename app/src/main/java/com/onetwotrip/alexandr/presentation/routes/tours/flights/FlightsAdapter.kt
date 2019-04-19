package com.onetwotrip.alexandr.presentation.routes.tours.flights

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.model.tours.Tour
import com.onetwotrip.alexandr.presentation.utils.NumberUtils
import kotlinx.android.synthetic.main.route_tours_flights_flight.view.*

class FlightsAdapter : RecyclerView.Adapter<FlightViewHolder>() {

    var onFlightOptionClick: ((flight: Tour.FlightOption) -> Unit)? = null

    private var flightOptions: List<Tour.FlightOption>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_tours_flights_flight, parent, false)
        return FlightViewHolder(view)
    }

    override fun getItemCount() = flightOptions?.size ?: 0

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flightOption = flightOptions!![position]
        holder.setData(flightOption)
        holder.onClick = {
            onFlightOptionClick?.invoke(flightOption)
        }
    }

    fun setData(flightOptions: List<Tour.FlightOption>) {
        var somethingChanged = false

        if(this.flightOptions != flightOptions) {
            this.flightOptions = flightOptions
            somethingChanged = true
        }

        if(somethingChanged) notifyDataSetChanged()
    }

}