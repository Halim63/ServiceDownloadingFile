package com.halim.downloadfile.repository.books.remote

import com.halim.downloadfile.model.GetBooksResponseModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import io.reactivex.rxjava3.core.Observable



interface BooksApiRetrofitInterface{

    @Streaming
    @GET
     fun downloadBook(@Url bookUrl: String): Observable<Response<ResponseBody>>

     @GET(BooksApiConstants.GET_BOOKS_API_URL)
     fun getBooks():Observable<GetBooksResponseModel>

}