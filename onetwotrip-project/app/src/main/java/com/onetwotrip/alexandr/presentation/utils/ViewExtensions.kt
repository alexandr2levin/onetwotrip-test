package com.onetwotrip.alexandr.presentation.utils

import android.view.View
import android.view.animation.AlphaAnimation

fun View.fadeIn(duration: Long, withAnimation: Boolean = true, changeVisibility: Boolean = true) {
    if(changeVisibility) visibility = View.VISIBLE
    if(withAnimation) {
        alpha = 0f
        animate()
                .alpha(1f)
                .setDuration(duration)
                .start()
    } else {
        alpha = 1f
    }
}

fun View.fadeOut(duration: Long, withAnimation: Boolean = true, changeVisibility: Boolean = true) {
    if(withAnimation) {
        alpha = 1f
        animate()
                .alpha(0f)
                .setDuration(duration)
                .withEndAction {
                    if(changeVisibility) visibility = View.GONE
                }
                .start()
    } else {
        alpha = 0f
        if(changeVisibility) visibility = View.GONE
    }
}