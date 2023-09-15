package com.halim.downloadfile.repository.books

import com.halim.downloadfile.repository.books.remote.BooksRemoteRepo
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
class BookRepo @Inject constructor(
    private val booksRemoteRepo: BooksRemoteRepo,
) {
     suspend fun downloadBook(bookUrl: String): Response<ResponseBody> {
        return booksRemoteRepo.downloadBook(bookUrl = bookUrl)
    }
}