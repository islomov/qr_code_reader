package com.epam.qrcodereader.data.repository

import com.epam.qrcodereader.data.network.ScanApi
import com.epam.qrcodereader.domain.repository.ScanResultsRepository

class ScanResultsRepositoryImpl(val apiService: ScanApi): ScanResultsRepository {

    override suspend fun upload(result: String?) = apiService.uploadScan(result)
}