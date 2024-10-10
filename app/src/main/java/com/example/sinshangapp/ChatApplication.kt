package com.example.sinshangapp

import android.app.Application
import com.example.sinshangapp.di.networkModule
import com.example.sinshangapp.di.serviceModule
import org.koin.core.context.GlobalContext.startKoin

class ChatApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(networkModule, serviceModule)
        }
    }
}