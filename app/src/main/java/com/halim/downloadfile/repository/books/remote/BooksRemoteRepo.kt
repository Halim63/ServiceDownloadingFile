package com.halim.downloadfile.repository.books.remote

import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

class BooksRemoteRepo @Inject constructor(
    private val booksApiRetrofitInterface: BooksApiRetrofitInterface,
) {
    suspend fun downloadBook(bookUrl: String) =
        booksApiRetrofitInterface.downloadBook(bookUrl = bookUrl)

}