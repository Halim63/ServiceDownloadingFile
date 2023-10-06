package com.halim.downloadfile.repository.books.remote

import com.halim.downloadfile.model.GetBooksResponseModel
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class BooksRemoteRepo @Inject constructor(
    private val booksApiRetrofitInterface: BooksApiRetrofitInterface,
) {
    fun downloadBook(bookUrl: String): Observable<Response<ResponseBody>> =
        booksApiRetrofitInterface.downloadBook(bookUrl = bookUrl)

    fun getBooks(): Observable<GetBooksResponseModel> = booksApiRetrofitInterface.getBooks()


}

