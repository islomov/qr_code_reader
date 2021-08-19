package com.epam.qrcodereader.koin

import com.epam.qrcodereader.domain.repository.ScanResultsRepository
import com.epam.qrcodereader.domain.usecase.UploadScanResultUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single{
        createUploadScanUseCase(get())
    }
}

fun createUploadScanUseCase(repository: ScanResultsRepository) = UploadScanResultUseCase(repository)