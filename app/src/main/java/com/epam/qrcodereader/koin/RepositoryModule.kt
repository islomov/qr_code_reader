package com.epam.qrcodereader.koin

import com.epam.qrcodereader.data.network.ScanApi
import com.epam.qrcodereader.data.repository.ScanResultsRepositoryImpl
import com.epam.qrcodereader.domain.repository.ScanResultsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<ScanResultsRepository> {
        createAppRepository(get())
    }
}


fun createAppRepository(api: ScanApi) : ScanResultsRepository {
    return ScanResultsRepositoryImpl(api)
}