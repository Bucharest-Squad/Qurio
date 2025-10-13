package com.bucharest.qurio

import android.app.Application
import com.bucharest.qurio.data.seed.DataSeeder
import com.bucharest.qurio.data.seed.SeedDataProvider
import com.bucharest.qurio.di.AppComponent
import com.bucharest.qurio.di.DaggerAppComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class QurioApp: Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        initDagger()
        seedDatabaseOnFirstLaunch()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
    
    private fun seedDatabaseOnFirstLaunch() {
        runBlocking(Dispatchers.IO) {
            val database = appComponent.getDatabase()
            DataSeeder(database, SeedDataProvider()).seedIfEmpty()
        }
    }
}