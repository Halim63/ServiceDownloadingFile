package com.example.downloadfile

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface RetrofitInterface {

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl:String): Response<ResponseBody>

}