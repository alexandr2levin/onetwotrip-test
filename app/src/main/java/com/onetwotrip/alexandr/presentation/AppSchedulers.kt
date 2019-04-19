package com.onetwotrip.alexandr.presentation

import io.reactivex.Scheduler

data class AppSchedulers(
    val mainScheduler: Scheduler,
    val backgroundScheduler: Scheduler
)