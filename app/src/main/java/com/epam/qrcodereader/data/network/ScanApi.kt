package com.epam.qrcodereader.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ScanApi {

    @POST("/")
    suspend fun uploadScan(@Body result: String?): Response<ResponseBody>

}