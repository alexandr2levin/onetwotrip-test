package com.onetwotrip.alexandr.presentation.routes.tours.filters

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.ViewGroup
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.data.companies.Company
import com.onetwotrip.alexandr.model.tours.SearchParams
import kotlinx.android.synthetic.main.route_tours_filters.*

class FiltersDialog(context: Context) : AlertDialog(context) {

    companion object {
        const val ARG_COMPANIES = "COMPANIES"
        const val ARG_SEARCH_PARAMS = "SEARCH_PARAMS"

        private const val KEY_COMPANIES = "COMPANIES"
        private const val KEY_SEARCH_PARAMS = "SEARCH_PARAMS"

        private const val KEY_RESULT_EVENT_FIRED = "RESULT_EVENT_FIRED"
    }

    var onDone: ((searchParams: SearchParams) -> Unit)? = null

    private lateinit var argCompanies: List<Company>
    private lateinit var argSearchParams: SearchParams

    private lateinit var companies: List<Company>
    private lateinit var searchParams: SearchParams

    private var adapter = CompaniesAdapter()

    private var resultEventFired = false

    fun setArgs(bundle: Bundle) {
        this.argCompanies = bundle.getParcelableArrayList(ARG_COMPANIES)!!
        this.argSearchParams = bundle.getParcelable(ARG_SEARCH_PARAMS)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.route_tours_filters)

        recycler_view.layoutManager = LinearLayoutManager(context)
        adapter.onCheckedCompaniesChanged = {
            searchParams = searchParams.copy(
                    companiesIds = adapter.checkedCompaniesIds!!.sorted()
            )
        }
        recycler_view.adapter = adapter

        done_button.setOnClickListener {
            fireOnDone()
        }
        reset_button.setOnClickListener {
            searchParams = searchParams.copy(companiesIds = emptyList())
            fireOnDone()
        }

        if(savedInstanceState?.containsKey(KEY_COMPANIES) == true) {
            companies = savedInstanceState.getParcelableArrayList(KEY_COMPANIES)!!
            searchParams = savedInstanceState.getParcelable(KEY_SEARCH_PARAMS)!!
        } else {
            companies = argCompanies
            searchParams = argSearchParams
        }

        adapter.setData(companies, searchParams.companiesIds)
    }

    override fun onSaveInstanceState(): Bundle {
        val outState = super.onSaveInstanceState()
        outState.putParcelableArrayList(KEY_COMPANIES, ArrayList(companies))
        outState.putParcelable(KEY_SEARCH_PARAMS, searchParams)

        outState.putBoolean(KEY_RESULT_EVENT_FIRED, resultEventFired)
        return outState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        resultEventFired = savedInstanceState.getBoolean(KEY_RESULT_EVENT_FIRED)
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun fireOnDone() {
        if(!resultEventFired) onDone?.invoke(searchParams)
        resultEventFired = true
    }

}