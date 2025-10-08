package com.bucharest.qurio

import android.app.Application
import com.bucharest.qurio.di.AppComponent
import com.bucharest.qurio.di.DaggerAppComponent

class QurioApp: Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}