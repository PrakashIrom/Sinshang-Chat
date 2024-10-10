package com.example.sinshangapp.di

import com.example.sinshangapp.data.ChatApiService
import com.example.sinshangapp.data.ChatApiServiceImpl
import com.example.sinshangapp.viewModel.ChatViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val networkModule = module{
    single{
        HttpClient(CIO){
            install(WebSockets)
        }
    }
}

val serviceModule = module{
    single<ChatApiService>{
        ChatApiServiceImpl(get())
    }
    viewModel{ ChatViewModel(get()) }
}