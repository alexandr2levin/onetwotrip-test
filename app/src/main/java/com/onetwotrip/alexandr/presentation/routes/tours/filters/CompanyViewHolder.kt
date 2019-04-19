package com.onetwotrip.alexandr.presentation.routes.tours.filters

import android.support.v7.widget.RecyclerView
import android.view.View
import com.onetwotrip.alexandr.data.companies.Company
import kotlinx.android.synthetic.main.route_tours_filters_company.view.*

class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var onClick: ((checked: Boolean) -> Unit)? = null

    private var checked: Boolean? = null

    init {
        itemView.setOnClickListener {
            val newValue = !checked!!
            itemView.check_box.isChecked = newValue
            checked = newValue
            onClick?.invoke(newValue)
        }
    }

    fun setData(company: Company, checked: Boolean) {
        val prevValue = this.checked
        this.checked = checked
        itemView.check_box.text = company.name
        if(prevValue != checked) {
            // to avoid extra animations or interrupting current pne
            itemView.check_box.isChecked = checked
        }
    }

}