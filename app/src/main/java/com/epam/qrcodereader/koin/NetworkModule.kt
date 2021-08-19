package com.epam.qrcodereader.koin

import com.epam.qrcodereader.BuildConfig
import com.epam.qrcodereader.data.network.ScanApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val retrofitModule = module {

    single {
        apiService(get())
    }

    single {
        url()
    }

}

fun url() = if(BuildConfig.DEBUG) BuildConfig.API_DEBUG_ENDPOINT else BuildConfig.API_RELEASE_ENDPOINT

fun apiService(url: String): ScanApi  {

    return Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ScanApi::class.java)

}