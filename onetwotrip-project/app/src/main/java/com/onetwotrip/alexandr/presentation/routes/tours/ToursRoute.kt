package com.onetwotrip.alexandr.presentation.routes.tours

import com.onetwotrip.alexandr.presentation.routes.base.MvpPresenter
import com.onetwotrip.alexandr.presentation.routes.base.MvpView
import javax.inject.Inject



interface ToursView : MvpView {

}

class ToursPresenter @Inject constructor(

) : MvpPresenter<ToursView>() {

    fun loadData() {

    }

}