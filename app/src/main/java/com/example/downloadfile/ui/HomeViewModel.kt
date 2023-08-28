package com.example.downloadfile.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.downloadfile.DownloadFileRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val downloadFileRepo: DownloadFileRepo,
) : ViewModel() {
    val downloadFileLiveData = MutableLiveData<InputStream>()
    private val url =
        "https://www.hsbc.co.uk/content/dam/hsbc/gb/pdf/hsbcuk-how-to-view-and-download-statements.pdf"

    fun downloadFile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = downloadFileRepo.downloadFile(url)
                val responseBody = response.body()
                downloadFileLiveData.postValue(responseBody?.byteStream())
            }catch (e:Exception){
                e.printStackTrace()
            }


        }


    }

}
