package com.halim.downloadfile.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halim.downloadfile.Resource
import com.halim.downloadfile.repository.books.BookRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

const val BOOK_URL =
    "https://www.hsbc.co.uk/content/dam/hsbc/gb/pdf/hsbcuk-how-to-view-and-download-statements.pdf"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepo: BookRepo,
) : ViewModel() {
    val downloadBookLiveData = MutableLiveData<Resource<InputStream>>()

    fun downloadBook() {
        downloadBookLiveData.value = Resource.loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = bookRepo.downloadBook(bookUrl = BOOK_URL)
                if (response.isSuccessful && response.body()?.byteStream() != null) {
                    downloadBookLiveData.postValue(Resource.success(response.body()!!.byteStream()))
                } else {
                    downloadBookLiveData.postValue(Resource.error(response.message()))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                downloadBookLiveData.postValue(Resource.error(e.message))
            }


        }


    }

}
