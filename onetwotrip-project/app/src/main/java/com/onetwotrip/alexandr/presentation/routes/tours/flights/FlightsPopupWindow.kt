package com.onetwotrip.alexandr.presentation.routes.tours.flights

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.PopupWindow
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.model.tours.Tour
import kotlinx.android.synthetic.main.route_tours_flights.view.*

class FlightsPopupWindow(
        private val context: Context,
        private val flightOptions: List<Tour.FlightOption>
) : PopupWindow(context) {

    var onFlightOptionClicked: ((flight: Tour.FlightOption) -> Unit)?
        get() = adapter.onFlightOptionClick
        set(value) {
            adapter.onFlightOptionClick = value
        }

    private val adapter = FlightsAdapter()

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.route_tours_flights, null)

        isOutsideTouchable = true
        isFocusable = true
        contentView = view
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        contentView.flights_recycler_view.layoutManager = LinearLayoutManager(context)
        contentView.flights_recycler_view.adapter = adapter

        disableAnchorParentScrolling()

        adapter.setData(flightOptions)
    }

    private fun measureSize(anchor: View) {
        val maxAvailableHeight = getMaxAvailableHeight(anchor)

        // workaround to achieve wrap_content for width and height
        contentView.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED)
        )

        width = contentView.measuredWidth

        if(contentView.measuredHeight > maxAvailableHeight) {
            height = maxAvailableHeight
        } else {
            height = contentView.measuredHeight
        }
    }

    fun showAsDropDownAccurate(anchor: View) {
        measureSize(anchor)
        showAsDropDown(anchor)
    }

    private fun disableAnchorParentScrolling() {
        val mSetAllowScrollingAnchorParent = PopupWindow::class.java.getDeclaredMethod(
                "setAllowScrollingAnchorParent",
                Boolean::class.javaPrimitiveType
        )
        mSetAllowScrollingAnchorParent.isAccessible = true
        mSetAllowScrollingAnchorParent.invoke(this, false)
    }

}