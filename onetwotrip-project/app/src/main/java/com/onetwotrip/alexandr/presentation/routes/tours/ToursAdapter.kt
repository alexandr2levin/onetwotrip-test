package com.onetwotrip.alexandr.presentation.routes.tours

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.data.companies.Company
import com.onetwotrip.alexandr.model.tours.SearchParams
import com.onetwotrip.alexandr.model.tours.Tour
import com.onetwotrip.alexandr.presentation.routes.tours.viewholders.FooterViewHolder
import com.onetwotrip.alexandr.presentation.routes.tours.viewholders.HeaderViewHolder
import com.onetwotrip.alexandr.presentation.routes.tours.viewholders.TourViewHolder
import java.lang.IllegalStateException

class ToursAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TOUR = 1
        private const val VIEW_TYPE_FOOTER = 2
    }

    private var companies: List<Company>? = null
    private var searchParams: SearchParams? = null
    private var tours: List<Tour>? = null

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> VIEW_TYPE_HEADER
            itemCount - 1 -> VIEW_TYPE_FOOTER
            else -> VIEW_TYPE_TOUR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.route_main_header, parent, false)

                HeaderViewHolder(view)
            }
            VIEW_TYPE_TOUR -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.route_main_tour, parent, false)

                TourViewHolder(view)
            }
            VIEW_TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.route_main_footer, parent, false)

                FooterViewHolder(view)
            }
            else -> throw IllegalStateException("no case for view type '$viewType'")
        }
    }

    fun setData(companies: List<Company>, searchParams: SearchParams, tours: List<Tour>) {
        var somethingChanged = false

        if(this.companies != companies) {
            this.companies = companies
            somethingChanged = true
        }
        if(this.searchParams != searchParams) {
            this.searchParams = searchParams
            somethingChanged = true
        }
        if(this.tours != tours) {
            this.tours = tours
            somethingChanged = true
        }

        if(somethingChanged) notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        var count = 0
        if(companies != null && searchParams != null) count += 1
        if(tours != null) count += tours!!.size
        count += 1 // for footer
        return count
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when(viewHolder) {
            is HeaderViewHolder -> {
                viewHolder.setData(companies!!, searchParams!!)
            }
            is TourViewHolder -> {
                val tourViewModel = tours!![position - 1]
                viewHolder.setData(tourViewModel)
            }
            is FooterViewHolder -> {
                // do nothing
            }
            else -> throw IllegalStateException("no case for view holder type '$viewHolder'")
        }
    }

}