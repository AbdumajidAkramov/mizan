package dev.esbi.mizan

import android.app.Application
import dev.esbi.mizan.di.AppComponent

class MizanApplication : Application() {

    internal lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = AppComponent(this)
    }
}
