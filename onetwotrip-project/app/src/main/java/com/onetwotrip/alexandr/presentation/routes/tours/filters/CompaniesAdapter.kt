package com.onetwotrip.alexandr.presentation.routes.tours.filters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.data.companies.Company

class CompaniesAdapter : RecyclerView.Adapter<CompanyViewHolder>() {

    val checkedCompaniesIds get() = _checkedCompaniesIds
    var onCheckedCompaniesChanged: (() -> Unit)? = null

    private var companies: List<Company>? = null
    private var _checkedCompaniesIds: List<Int>? = null

    init {
        setHasStableIds(true)
    }

    fun setData(companies: List<Company>, checkedCompaniesIds: List<Int>) {
        var somethingChanged = false

        if(this.companies != companies) {
            this.companies = companies
            somethingChanged = true
        }
        if(this._checkedCompaniesIds != checkedCompaniesIds) {
            this._checkedCompaniesIds = checkedCompaniesIds
            somethingChanged = true
        }
        if(!somethingChanged) notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return companies!![position].id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_tours_filters_company, parent, false)

        return CompanyViewHolder(view)
    }

    override fun getItemCount() = companies?.size ?: 0

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val company = companies!![position]
        val checked = _checkedCompaniesIds!!.contains(company.id)
        holder.onClick = {
            if(_checkedCompaniesIds!!.contains(company.id)) {
                _checkedCompaniesIds = _checkedCompaniesIds!!.minus(company.id)
            } else {
                _checkedCompaniesIds = _checkedCompaniesIds!!.plus(company.id)
            }
            onCheckedCompaniesChanged?.invoke()
        }
        holder.setData(company, checked)
    }
}