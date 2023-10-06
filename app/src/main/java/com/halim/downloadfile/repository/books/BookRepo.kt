package com.halim.downloadfile.repository.books

import com.halim.downloadfile.model.GetBooksResponseModel
import com.halim.downloadfile.repository.books.remote.BooksRemoteRepo
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class BookRepo @Inject constructor(
    private val booksRemoteRepo: BooksRemoteRepo,
) {
    fun downloadBook(bookUrl: String): Observable<Response<ResponseBody>> {
        return booksRemoteRepo.downloadBook(bookUrl = bookUrl)
    }


    fun getBooks(): Observable<GetBooksResponseModel> {
        return booksRemoteRepo.getBooks()
    }
}