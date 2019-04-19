package com.onetwotrip.alexandr.presentation.routes.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.onetwotrip.alexandr.presentation.ComponentsHolder
import com.onetwotrip.alexandr.presentation.PresentersHolder
import org.slf4j.LoggerFactory
import java.util.*

abstract class MvpActivity<V : MvpView, P : MvpPresenter<V>> : AppCompatActivity() {

    companion object {
        private const val KEY_VIEW_ID = "VIEW_ID"
    }

    protected val viewId get() = _viewId

    private lateinit var _viewId: String

    protected val presenter: P
        get() = _presenter ?: throw IllegalStateException("trying to access presenter while it isn't attached")

    private var _presenter: P? = null

    private var freshActivity: Boolean? = null

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            freshActivity = true
            // fresh activity started
            _viewId = UUID.randomUUID().toString()
        } else {
            freshActivity = false
            val viewId = savedInstanceState.getString(KEY_VIEW_ID)
            if(viewId == null) {
                throw IllegalStateException(
                    "there is no view id in the savedInstanceState '$savedInstanceState'"
                )
            }
            this._viewId = viewId
        }
        configureDependencyInjection()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_VIEW_ID, _viewId)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if(!PresentersHolder.contains(_viewId)) {
            createPresenter()
        }
        attachPresenter()
        if(freshActivity!!) {
            onResumeFreshActivity()
        }
    }

    override fun onPause() {
        detachPresenter()
        super.onPause()
    }

    override fun onDestroy() {
        if(!isChangingConfigurations) {
            destroyPresenter()
            ComponentsHolder.removeAllWithTag(viewId)
        }
        super.onDestroy()
    }

    abstract fun provideMvpView(): V
    abstract fun provideNewPresenter(): P
    abstract fun onResumeFreshActivity()
    abstract fun configureDependencyInjection()

    private fun createPresenter() {
        val presenter = provideNewPresenter()
        logger.debug("create presenter '$presenter'")
        PresentersHolder.put(_viewId, presenter)
    }

    private fun attachPresenter() {
        _presenter = PresentersHolder.get(_viewId)
        logger.debug("attach presenter '$_presenter'")
        _presenter!!.attachView(provideMvpView())
    }

    private fun detachPresenter() {
        logger.debug("detach presenter '$_presenter'")
        _presenter!!.detachView()
        _presenter = null
    }

    private fun destroyPresenter() {
        val presenter = PresentersHolder.get<P>(_viewId)
        logger.debug("destroy presenter '$presenter'")
        presenter.destroy()
        PresentersHolder.remove(_viewId)
    }

}