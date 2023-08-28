package com.example.downloadfile

import javax.inject.Inject

class DownloadFileRepo @Inject constructor(
      private val retrofitInterface: RetrofitInterface
) {
    suspend fun downloadFile(url: String) = retrofitInterface.downloadFile(fileUrl = url)
}