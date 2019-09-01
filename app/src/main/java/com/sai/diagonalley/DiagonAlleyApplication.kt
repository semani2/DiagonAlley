package com.sai.diagonalley

import android.app.Application
import timber.log.Timber

class DiagonAlleyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
