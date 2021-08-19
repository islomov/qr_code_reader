package com.epam.qrcodereader

import android.app.Application
import com.epam.qrcodereader.koin.applicationComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        setUpKoin()
    }

    fun setUpKoin(){
        startKoin {
            androidContext(this@App)
            modules(applicationComponent)
        }
    }
}