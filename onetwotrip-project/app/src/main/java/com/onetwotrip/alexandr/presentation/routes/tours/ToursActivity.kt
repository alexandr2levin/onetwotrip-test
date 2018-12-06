package com.onetwotrip.alexandr.presentation.routes.tours

import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewTreeObserver
import android.widget.Toast
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.data.companies.Company
import com.onetwotrip.alexandr.model.tours.SearchParams
import com.onetwotrip.alexandr.model.tours.Tour
import com.onetwotrip.alexandr.presentation.ComponentsHolder
import com.onetwotrip.alexandr.presentation.di.AppComponent
import com.onetwotrip.alexandr.presentation.routes.base.MvpActivity
import com.onetwotrip.alexandr.presentation.routes.tours.filters.FiltersDialog
import com.onetwotrip.alexandr.presentation.routes.tours.flights.FlightsPopupWindow
import com.onetwotrip.alexandr.presentation.routes.tours.viewholders.TourViewHolder
import com.onetwotrip.alexandr.presentation.utils.fadeIn
import com.onetwotrip.alexandr.presentation.utils.fadeOut
import kotlinx.android.synthetic.main.route_tours.*
import kotlinx.android.synthetic.main.route_tours_error.view.*
import org.flycraft.tonguetwisterslibrary.android.presentation.utils.Emoji
import org.slf4j.LoggerFactory
import kotlin.IllegalStateException

class ToursActivity : MvpActivity<ToursView, ToursPresenter>(), ToursView {

    companion object {
        private var KEY_VIEW_MODEL = "VIEW_MODEL"
        private var KEY_SELECTED_TOUR = "SELECTED_TOUR"

        private var ID_FILTERS_DIALOG = 280
    }

    override var viewModel: ToursViewModel?
        get() = _viewModel
        set(value) {
            if(value == null) throw IllegalStateException("you mustn't set viewModel to null")
            val prevViewModel = _viewModel
            _viewModel = value
            applyViewModel(value, prevViewModel != null && prevViewModel::class != value::class)

            if(prevViewModel != value) {
                selectedTour = null // reset selection on viewModel changes
            }
        }

    private var _viewModel: ToursViewModel? = null

    private var selectedTour: Tour?
        get() = _selectedTour
        set(value) {
            if(this._selectedTour == value) return
            _selectedTour = value

            applySelectedTour(value)
        }
    private var _selectedTour: Tour? = null

    private var flightsPopupWindow: FlightsPopupWindow? = null

    private lateinit var adapter: ToursAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var _component: ToursComponent

    private val logger = LoggerFactory.getLogger(javaClass)

    private fun applyViewModel(viewModel: ToursViewModel, withAnim: Boolean) {
        val animationDuration = 200L
        when(viewModel.state) {
            ToursViewModel.State.LOADING -> {
                loading_view.fadeIn(animationDuration, withAnim)
                error_view.fadeOut(animationDuration, withAnim)
                val companies = viewModel.companies ?: emptyList()
                adapter.setData(companies, viewModel.searchParams, emptyList())
            }
            ToursViewModel.State.SUCCESS -> {
                loading_view.fadeOut(animationDuration, withAnim)
                error_view.fadeOut(animationDuration, withAnim)
                adapter.setData(viewModel.companies!!, viewModel.searchParams, viewModel.tours!!)
            }
            ToursViewModel.State.ERROR -> {
                loading_view.fadeOut(animationDuration, withAnim)
                error_view.fadeIn(animationDuration, withAnim)
                val companies = viewModel.companies ?: emptyList()
                adapter.setData(companies, viewModel.searchParams, emptyList())
            }
            else -> throw IllegalStateException("no case for view model type '$viewModel'")
        }
    }

    //region Mvp configuration
    override fun provideMvpView() = this
    override fun provideNewPresenter() = _component.presenter()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.route_tours)

        layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        adapter = ToursAdapter()
        recycler_view.adapter = adapter

        adapter.onFilterClicked = {
            showFilersDialog(viewModel!!.companies!!, viewModel!!.searchParams)
        }
        adapter.onTourClick = { tour ->
            selectedTour = tour
        }

        error_view.retry_button.setOnClickListener {
            presenter.loadData()
        }

        _viewModel = savedInstanceState?.getParcelable(KEY_VIEW_MODEL)
        if(_viewModel != null) {
            applyViewModel(viewModel!!, false)
        }
        _selectedTour = savedInstanceState?.getParcelable(KEY_SELECTED_TOUR)
        if(_selectedTour != null) {
            applySelectedTour(_selectedTour!!)
        }
    }

    // I really think that it is a good way to handle dialogs
    @Suppress("DEPRECATION")
    override fun onCreateDialog(id: Int, args: Bundle?): Dialog? {
        return when(id) {
            ID_FILTERS_DIALOG -> {
                FiltersDialog(this).apply {
                    setArgs(args!!)
                    onDone = { searchParams ->
                        if(searchParams != viewModel!!.searchParams) {
                            this@ToursActivity.viewModel = viewModel!!.copy(searchParams = searchParams)
                            presenter.loadData()
                        }
                        removeDialog(ID_FILTERS_DIALOG)
                    }
                    setOnCancelListener {
                        removeDialog(ID_FILTERS_DIALOG)
                    }
                }
            }
            else -> throw IllegalStateException("no case for dialog id '$id'")
        }
    }

    override fun onResume() {
        super.onResume()
        if(_selectedTour != null && flightsPopupWindow == null) applySelectedTour(_selectedTour)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_VIEW_MODEL, _viewModel)
        outState.putParcelable(KEY_SELECTED_TOUR, _selectedTour)
    }

    override fun onPause() {
        // to avoid android.view.WindowLeaked
        if(_selectedTour != null && flightsPopupWindow != null) {
            flightsPopupWindow?.setOnDismissListener(null)
            applySelectedTour(null)
        }
        super.onPause()
    }

    private fun applySelectedTour(tour: Tour?) {
        if(tour != null) {
            val flightsPopupWindow = FlightsPopupWindow(this, tour.flightOptions)
            flightsPopupWindow.onFlightOptionClicked = {
                val message = Emoji.replaceAliasWithEmoji(getString(R.string.route_tour_done_message))
                Toast.makeText(this, message, Toast.LENGTH_LONG)
                        .show()
                selectedTour = null
            }
            this.flightsPopupWindow = flightsPopupWindow
            adapter.highlightTour(tour)

            flightsPopupWindow.setOnDismissListener {
                adapter.dehighlightTour()
                selectedTour = null
            }

            tryToShowFlights(flightsPopupWindow, tour)
        } else {
            flightsPopupWindow?.dismiss()
            flightsPopupWindow = null
        }
    }

    private fun tryToShowFlights(flightsPopupWindow: FlightsPopupWindow, tour: Tour) {
        recycler_view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                // do on the next draw call as findFirstCompletelyVisibleItemPosition returns
                // null just after orientation changes

                if(selectedTour == tour) {
                    // if selectedTour doesn't changed since the scheduling – continue
                    val firstVisibleAdapterPos = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val lastVisibleAdapterPos = layoutManager.findLastCompletelyVisibleItemPosition()
                    val highlightedAdapterPosition = adapter.getHighlightedTourAdapterPosition()

                    if(highlightedAdapterPosition !in (firstVisibleAdapterPos..lastVisibleAdapterPos)) {
                        recycler_view.scrollToPosition(highlightedAdapterPosition)
                    }

                    recycler_view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            // do on the next draw call as scrollToPosition only starts to act
                            // on th next draw call

                            val anchorView = (recycler_view.findViewHolderForAdapterPosition(
                                    highlightedAdapterPosition
                            )!! as TourViewHolder).popupAnchorView
                            flightsPopupWindow.showAsDropDownAccurate(anchorView)
                            recycler_view.viewTreeObserver.removeOnPreDrawListener(this)
                            return true
                        }
                    })
                }
                recycler_view.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
    }

    private fun showFilersDialog(companies: List<Company>, searchParams: SearchParams) {
        // see explanation above onCreateDialog()
        @Suppress("DEPRECATION")
        showDialog(ID_FILTERS_DIALOG, Bundle().apply {
            putParcelableArrayList(FiltersDialog.ARG_COMPANIES, ArrayList(companies))
            putParcelable(FiltersDialog.ARG_SEARCH_PARAMS, searchParams)
        })
    }

    override fun configureDependencyInjection() {
        // persist Activity's component across orientation changes for cases
        // when component holds persistable instances
        // (not this app's case, but this is good for extensibility)

        // it also nice that one Activity can have multiple components
        if(!ComponentsHolder.any(viewId, ToursComponent::class)) {
            val component = DaggerToursComponent.builder()
                .appComponent(ComponentsHolder.getDefault<AppComponent>())
                .build()
            ComponentsHolder.add(viewId, ToursComponent::class, component)
        }
        _component = ComponentsHolder.get(viewId, ToursComponent::class)
    }

    override fun onResumeFreshActivity() {
        presenter.initializeViewModel()
        presenter.loadData()
    }

}
