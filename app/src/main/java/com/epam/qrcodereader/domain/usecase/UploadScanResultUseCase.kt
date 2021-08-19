package com.epam.qrcodereader.domain.usecase

import com.epam.qrcodereader.domain.repository.ScanResultsRepository

class UploadScanResultUseCase (private val repository: ScanResultsRepository) {

    suspend fun uploadRepository(result: String?) = repository.upload(result)

}