package com.epam.qrcodereader.koin

import com.epam.qrcodereader.ui.qrResult.QrResultViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        QrResultViewModel(get())
    }
}