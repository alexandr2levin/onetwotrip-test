package com.onetwotrip.alexandr.presentation

import com.onetwotrip.alexandr.presentation.routes.base.MvpPresenter
import com.onetwotrip.alexandr.presentation.routes.base.MvpView

/**
 * Just a place to keep all Presenters in sweet place
 */
object PresentersHolder {

    private val map = hashMapOf<String, MvpPresenter<*>>()

    fun put(key: String, presenter: MvpPresenter<*>) {
        if(map.containsKey(key)) {
            throw IllegalStateException("PresenterHolder already contains presenter with key '$key'")
        }
        map[key] = presenter
    }

    fun remove(key: String) {
        if(map.containsKey(key)) {
            throw IllegalStateException("there is no presenter with key '$key' in PresenterHolder")
        }
        map.remove(key)
    }

    fun <V : MvpView, P : MvpPresenter<V>> get(key: String): P {
        if(map.containsKey(key)) {
            throw IllegalStateException("there is no presenter with key '$key' in PresenterHolder")
        }
        return map[key] as P
    }

    fun contains(key: String): Boolean {
        return map.contains(key)
    }

}