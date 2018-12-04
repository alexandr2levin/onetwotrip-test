package com.onetwotrip.alexandr.presentation.routes.tours

import com.onetwotrip.alexandr.presentation.AppSchedulers
import com.onetwotrip.alexandr.presentation.di.AppComponent
import com.onetwotrip.alexandr.presentation.di.ViewScope
import com.onetwotrip.alexandr.presentation.routes.base.MvpPresenter
import com.onetwotrip.alexandr.presentation.routes.base.MvpView
import dagger.Component
import javax.inject.Inject

@ViewScope
@Component(dependencies = [AppComponent::class])
interface ToursRouteComponent : AppComponent {
    fun presenter(): ToursPresenter
}

interface ToursView : MvpView {

}

class ToursPresenter @Inject constructor(
    private val appSchedulers: AppSchedulers
) : MvpPresenter<ToursView>() {

    fun loadData() {

    }

}