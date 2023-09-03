package com.halim.downloadfile.repository.books.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface BooksApiRetrofitInterface {

    @Streaming
    @GET
    suspend fun downloadBook(@Url bookUrl:String): Response<ResponseBody>

}