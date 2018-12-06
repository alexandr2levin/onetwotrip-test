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
import org.slf4j.LoggerFactory
import kotlin.IllegalStateException

class ToursAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_TOUR = 1
        private const val VIEW_TYPE_FOOTER = 2
    }

    var onTourClick: ((tour: Tour) -> Unit)? = null
    var onFilterClicked: (() -> Unit)? = null

    private var companies: List<Company>? = null
    private var searchParams: SearchParams? = null
    private var tours: List<Tour>? = null

    private var highlightedTour: Tour? = null

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val viewType = getItemViewType(position)
        return when(viewType) {
            VIEW_TYPE_HEADER -> 0L
            VIEW_TYPE_FOOTER -> 1L
            VIEW_TYPE_TOUR -> {
                2L + tours!![getTourIndexByAdapterPosition(position)].hotelId
            }
            else -> throw IllegalStateException("no case for view type '$viewType'")
        }
    }

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
                        .inflate(R.layout.route_tours_header, parent, false)

                HeaderViewHolder(view)
            }
            VIEW_TYPE_TOUR -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.route_tours_tour, parent, false)

                TourViewHolder(view)
            }
            VIEW_TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.route_tours_footer, parent, false)

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

    fun highlightTour(tour: Tour) {
        if(this.highlightedTour == tour) return
        this.highlightedTour = tour

        if(tours != null) {
            val tourIndex = tours!!.indexOf(tour)
            notifyItemChanged(getAdapterPositionByTourIndex(tourIndex))
        } else {
            // otherwise it will be updated during setData(...)'s notifyDataSetChanged()
        }
    }

    fun dehighlightTour() {
        if(this.highlightedTour == null) return
        val prevHighlightedTour = this.highlightedTour
        this.highlightedTour = null

        if(tours != null) {
            val tourIndex = tours!!.indexOf(prevHighlightedTour!!)
            notifyItemChanged(getAdapterPositionByTourIndex(tourIndex))
        } else {
            // otherwise it will be updated during setData(...)'s notifyDataSetChanged()
        }
    }

    fun getHighlightedTourAdapterPosition(): Int {
        if(tours == null) {
            throw IllegalStateException("there is no tours in adapter to find adapter position of")
        }
        if(highlightedTour == null) {
            throw IllegalStateException("there is no highlightedTour in adapter to find adapter position of it")
        }

        return getAdapterPositionByTourIndex(tours!!.indexOf(highlightedTour!!))
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
                viewHolder.onFilterClicked = {
                    onFilterClicked?.invoke()
                }
                viewHolder.setData(companies!!, searchParams!!)
            }
            is TourViewHolder -> {
                val tour = tours!![getTourIndexByAdapterPosition(position)]
                val highlighted = tour == highlightedTour
                viewHolder.onClick = {
                    onTourClick?.invoke(tour)
                }
                viewHolder.setData(tour, highlighted)
            }
            is FooterViewHolder -> {
                // do nothing
            }
            else -> throw IllegalStateException("no case for view holder type '$viewHolder'")
        }
    }

    private fun getTourIndexByAdapterPosition(adapterPosition: Int): Int {
        if(adapterPosition == 0) {
            throw IllegalStateException("adapterPosition for tour can't be '0'")
        }
        return adapterPosition - 1
    }

    private fun getAdapterPositionByTourIndex(tourIndex: Int): Int {
        return tourIndex + 1
    }

}