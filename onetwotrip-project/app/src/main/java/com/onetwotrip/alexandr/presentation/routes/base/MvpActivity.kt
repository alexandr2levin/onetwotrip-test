package com.onetwotrip.alexandr.presentation.routes.base

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.onetwotrip.alexandr.presentation.PresentersHolder
import java.util.*

abstract class MvpActivity<V : MvpView, P : MvpPresenter<V>> : AppCompatActivity() {

    private lateinit var viewId: String

    protected val presenter: P
        get() = _presenter ?: throw IllegalStateException("trying to access presenter while it isn't attached")

    private var _presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        if(savedInstanceState == null) {
            // fresh activity started
            viewId = UUID.randomUUID().toString()
            createPresenter()
            onFreshActivityCreate()
        }
    }

    abstract fun provideNewPresenter(): P
    abstract fun provideView(): V
    abstract fun onFreshActivityCreate()

    override fun onResume() {
        super.onResume()
        attachPresenter()
    }

    override fun onPause() {
        detachPresenter()
        super.onPause()
    }

    override fun onDestroy() {
        if(!isChangingConfigurations) {
            destroyPresenter()
        }
        super.onDestroy()
    }

    private fun createPresenter() {
        val newPresenter = provideNewPresenter()
        PresentersHolder.put(viewId, newPresenter)
    }

    private fun attachPresenter() {
        presenter.attachView(provideView())
    }

    private fun detachPresenter() {
        presenter.detachView()
    }

    private fun destroyPresenter() {
        presenter.destroy()
        PresentersHolder.remove(viewId)
    }

}