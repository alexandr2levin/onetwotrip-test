package com.onetwotrip.alexandr.presentation.routes.tours.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.model.tours.Tour
import com.onetwotrip.alexandr.presentation.utils.NumberUtils
import kotlinx.android.synthetic.main.route_tours_tour.view.*

class TourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val popupAnchorView = itemView.price_text_view

    var onClick: (() -> Unit)? = null

    private val resources = itemView.resources

    private var tour: Tour? = null
    private var highlighted: Boolean? = null

    init {
        itemView.card_view.setOnClickListener {
            onClick?.invoke()
        }
    }

    fun setData(tour: Tour, highlighted: Boolean) {
        val prevHighlighted = this.highlighted
        this.tour = tour
        this.highlighted = highlighted

        val playAnim = prevHighlighted != null
        changeHighlighted(highlighted, playAnim)

        val flightOptionsCount = tour.flightOptions.size
        val cheapestFlightOption = tour.flightOptions.first()

        itemView.hotel_name_text_view.text = tour.hotelName

        val priceText = NumberUtils.groupSeparate(
            tour.flightOptions.first().priceWithHotel,
            ' '
        )
        if(flightOptionsCount == 1) {
            itemView.flight_info_text_view.text = cheapestFlightOption.companyName
            itemView.price_text_view.text = resources.getString(R.string.route_tours_header_single_price, priceText)
        } else {
            itemView.flight_info_text_view.text = resources.getQuantityString(
                R.plurals.route_tours_tour,
                flightOptionsCount,
                flightOptionsCount
            )
            itemView.price_text_view.text = resources.getString(R.string.route_tours_header_min_price, priceText)
        }
    }

    fun changeHighlighted(highlighted: Boolean, withAnim: Boolean) {
        if(highlighted) {
            if(withAnim) {
                itemView.card_view.animate()
                        .alpha(0.5f)
                        .setDuration(200)
            } else {
                itemView.card_view.alpha = 0.5f
            }
        } else {
            if(withAnim) {
                itemView.card_view.animate()
                        .alpha(1f)
                        .setDuration(200)
            } else {
                itemView.card_view.alpha = 1f
            }
        }
    }

}