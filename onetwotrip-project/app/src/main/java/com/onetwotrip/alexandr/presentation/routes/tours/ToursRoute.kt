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
                view.viewModel = ToursViewModel.Loading(
                    searchParams = SearchParams(
                        companiesIds = listOf()
                    )
                )
            }
        }
    }

    fun loadData() {
        onceViewAttached { view ->
            view.viewModel = ToursViewModel.Loading(
                    searchParams = view.viewModel!!.searchParams
            )

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
        }
    }

    private fun applySuccessViewModel(tours: List<Tour>, companies: List<Company>) {
        onceViewAttached { view ->
            view.viewModel = ToursViewModel.Success(
                searchParams = view.viewModel!!.searchParams,
                companies = companies,
                tours = tours
            )
        }
    }

    private fun applyErrorViewModel() {
        onceViewAttached { view ->
            view.viewModel = ToursViewModel.Error(
                searchParams = view.viewModel!!.searchParams
            )
        }
    }

}


sealed class ToursViewModel : Parcelable {

    abstract val searchParams: SearchParams

    @Parcelize
    data class Success(
        override val searchParams: SearchParams,
        val companies: List<Company>,
        val tours: List<Tour>
    ) : ToursViewModel()

    @Parcelize
    data class Error(
        override val searchParams: SearchParams
    ) : ToursViewModel()

    @Parcelize
    data class Loading(
        override val searchParams: SearchParams
    ) : ToursViewModel()

}