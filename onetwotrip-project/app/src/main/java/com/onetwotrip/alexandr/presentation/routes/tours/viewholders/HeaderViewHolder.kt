package com.onetwotrip.alexandr.presentation.routes.tours.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.data.companies.Company
import com.onetwotrip.alexandr.model.tours.SearchParams
import kotlinx.android.synthetic.main.route_main_header.view.*

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val resources = itemView.resources

    fun setData(companies: List<Company>, searchParams: SearchParams) {
        if(searchParams.companiesIds.isEmpty()) {
            itemView.header_text_view.text = resources.getString(
                R.string.route_main_header
            )
        } else {
            val filterCompaniesNames = searchParams.companiesIds.map { targetId ->
                searchCompanyNameById(targetId, companies)
            }
            itemView.header_text_view.text = resources.getString(
                R.string.route_main_header_with_filters,
                filterCompaniesNames.joinToString(separator = ", ")
            )
        }
    }

    private fun searchCompanyNameById(id: Int, companies: List<Company>): String {
        return companies.first { it.id == id }.name
    }

}