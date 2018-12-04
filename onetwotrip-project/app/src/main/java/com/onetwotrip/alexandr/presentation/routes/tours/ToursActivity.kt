package com.onetwotrip.alexandr.presentation.routes.tours

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.onetwotrip.alexandr.R
import com.onetwotrip.alexandr.presentation.routes.base.MvpActivity

class ToursActivity : MvpActivity<ToursView, ToursPresenter>(), ToursView {

    override fun provideNewPresenter() = TODO()

    override fun provideView() = this

    override fun onFreshActivityCreate() {
        presenter.loadData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
