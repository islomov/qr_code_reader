package com.epam.qrcodereader.domain.repository

import okhttp3.ResponseBody
import retrofit2.Response

interface ScanResultsRepository {

    suspend fun upload(result: String?): Response<ResponseBody>

}