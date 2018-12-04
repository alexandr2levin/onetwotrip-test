package com.onetwotrip.alexandr.presentation.routes.base

import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException

abstract class MvpPresenter<V : MvpView> {

    val destroyCompositeDisposable: CompositeDisposable
        get() {
            if(destroyed) {
                throw IllegalStateException("presenter already destroyed")
            }
            if(_destroyCompositeDisposable.isDisposed) {
                throw IllegalStateException("presenterDestroyCompositeDisposable already disposed")
            }
            return _destroyCompositeDisposable
        }

    private val _destroyCompositeDisposable = CompositeDisposable()

    private var _view: V? = null
    private val pendingViewActions = mutableListOf<(view: V) -> Unit>()

    protected var destroyed = false
        private set

    protected val detached get() = _view == null

    private val logger = LoggerFactory.getLogger(javaClass)

    fun onceViewAttached(viewAction: (view: V) -> Unit) {
        if(destroyed) {
            throw IllegalStateException("trying to post view action for destroyed presenter")
        }
        val view = _view
        if(view != null) {
            viewAction(view)
        } else {
            pendingViewActions.add(viewAction)
        }
    }

    fun attachView(view: V) {
        if(destroyed) {
            throw IllegalStateException("trying to attach destroyed presenter")
        }
        if(_view != null) {
            throw IllegalStateException("view already attached")
        }
        _view = view

        pendingViewActions.forEach { pendingViewAction -> pendingViewAction(view) }
        pendingViewActions.clear()
    }

    open fun detachView() {
        if(destroyed) {
            throw IllegalStateException("trying to detachView while presenter destroyed")
        }
        if(_view == null) {
            throw IllegalStateException("view already detached")
        }
        _view = null
    }

    open fun destroy() {
        if(destroyed) {
            throw IllegalStateException("presenter already detached")
        }
        if(_view != null) {
            throw IllegalStateException("presenter can't be destroyed while attached to view '$_view'")
        }
        destroyCompositeDisposable.dispose()
        destroyed = true
    }

}