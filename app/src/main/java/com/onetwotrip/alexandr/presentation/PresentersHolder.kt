package com.onetwotrip.alexandr.presentation

import com.onetwotrip.alexandr.presentation.routes.base.MvpPresenter
import org.slf4j.LoggerFactory

/**
 * Just a place to keep all Presenters in sweet place
 */
object PresentersHolder {

    private val presenters = hashMapOf<String, MvpPresenter<*>>()

    private val logger = LoggerFactory.getLogger(javaClass)

    fun put(key: String, presenter: MvpPresenter<*>) {
        if(presenters.containsKey(key)) {
            throw IllegalStateException("PresenterHolder already contains presenter with key '$key'")
        }
        presenters[key] = presenter
    }

    fun remove(key: String) {
        if(!presenters.containsKey(key)) {
            throw IllegalStateException("there is no presenter with key '$key' in PresenterHolder")
        }
        presenters.remove(key)
    }

    fun <P : MvpPresenter<*>> get(key: String): P {
        if(!presenters.containsKey(key)) {
            throw IllegalStateException("there is no presenter with key '$key' in PresenterHolder")
        }
        return presenters[key] as P
    }

    fun contains(key: String): Boolean {
        return presenters.contains(key)
    }

    private fun printState() {
        logger.debug("presenters count '${presenters.size}'")
    }

}