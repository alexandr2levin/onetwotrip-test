package com.onetwotrip.alexandr.presentation.routes.tours

import android.os.Parcelable
import com.onetwotrip.alexandr.data.companies.Company
import com.onetwotrip.alexandr.model.tours.Tour
import com.onetwotrip.alexandr.model.tours.SearchParams
import com.onetwotrip.alexandr.model.tours.ToursManager
import com.onetwotrip.alexandr.presentation.AppSchedulers
import com.onetwotrip.alexandr.presentation.di.AppComponent
import com.onetwotrip.alexandr.presentation.di.ViewScope
import com.onetwotrip.alexandr.presentation.routes.base.MvpPresenter
import com.onetwotrip.alexandr.presentation.routes.base.MvpView
import dagger.Component
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.zipWith
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@ViewScope
@Component(dependencies = [AppComponent::class])
interface ToursComponent : AppComponent {
    fun presenter(): ToursPresenter
}

interface ToursView : MvpView {
    var viewModel: ToursViewModel?
}

class ToursPresenter @Inject constructor(
    private val appSchedulers: AppSchedulers,
    private val toursManager: ToursManager
) : MvpPresenter<ToursView>() {

    fun initializeViewModel() {
        onceViewAttached { view ->
            if(view.viewModel == null) {
                view.viewModel = ToursViewModel(
                        state = ToursViewModel.State.LOADING,
                        searchParams = SearchParams(
                            companiesIds = listOf()
                        ),
                        companies = null,
                        tours = null
                )
            }
        }
    }

    fun loadData() {
        onceViewAttached { view ->
            view.viewModel = view.viewModel!!.copy(state = ToursViewModel.State.LOADING)

            toursManager.search(view.viewModel!!.searchParams)
                .zipWith(toursManager.getCompanies()) {
                        tours, companies -> tours to companies
                }
                .subscribeOn(appSchedulers.backgroundScheduler)
                .observeOn(appSchedulers.mainScheduler)
                .subscribeBy(
                    onSuccess = { (tours, companies) ->
                        applySuccessViewModel(tours, companies)
                    },
                    onError = {
                        applyErrorViewModel()
                    }
                )
                .addTo(destroyCompositeDisposable)
        }
    }

    private fun applySuccessViewModel(tours: List<Tour>, companies: List<Company>) {
        onceViewAttached { view ->
            view.viewModel = view.viewModel!!.copy(
                    state = ToursViewModel.State.SUCCESS,
                    companies = companies,
                    searchParams = view.viewModel!!.searchParams,
                    tours = tours
            )
        }
    }

    private fun applyErrorViewModel() {
        onceViewAttached { view ->
            view.viewModel = view.viewModel!!.copy(state = ToursViewModel.State.ERROR)
        }
    }

}

@Parcelize
data class ToursViewModel(
        val state: State,
        val searchParams: SearchParams,
        val companies: List<Company>?,
        val tours: List<Tour>?
) : Parcelable {
    enum class State {
        LOADING,
        ERROR,
        SUCCESS
    }
}