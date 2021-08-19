package com.epam.qrcodereader.ui.qrResult

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epam.qrcodereader.domain.usecase.UploadScanResultUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrResultViewModel(private val uploadUseCase: UploadScanResultUseCase): ViewModel() {

    private val _uiEvent: MutableLiveData<UIEvent> = MutableLiveData()
    val uiEvent: LiveData<UIEvent> = _uiEvent


    fun upload(result: String?){
        viewModelScope.launch {

            _uiEvent.value = UIEvent.UIEventLoading

            withContext(Dispatchers.IO) {
                val response = uploadUseCase.uploadRepository(result)
                if (response.isSuccessful) {
                    Log.i("Islomov","upload success:${response.body()}")
                    _uiEvent.postValue(UIEvent.UIEventSuccess(response.body()))
                } else {
                    Log.i("Islomov","upload error:${response.errorBody()}")
                    _uiEvent.postValue(UIEvent.UIEventError(response.errorBody()?.string()))
                }
            }

        }
    }

}

sealed class UIEvent {
    class UIEventSuccess(val data: Any?) : UIEvent()
    class UIEventError(val message: String?): UIEvent()
    object UIEventLoading: UIEvent()
}