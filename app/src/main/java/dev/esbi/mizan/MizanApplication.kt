package dev.esbi.mizan

import android.app.Application
import dev.esbi.mizan.di.AppComponent
import dev.esbi.mizan.di.DaggerAppComponent

class MizanApplication : Application() {
    
    lateinit var appComponent: AppComponent
        private set
    
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}
