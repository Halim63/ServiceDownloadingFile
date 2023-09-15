package com.halim.downloadfile.di

import com.halim.downloadfile.repository.books.remote.BooksApiRetrofitInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = "https://hub.dummyapis.com/"

    @Provides
    @Singleton
    fun provideBooksApiRetrofitInterface(retrofit: Retrofit): BooksApiRetrofitInterface =
        retrofit.create(BooksApiRetrofitInterface::class.java)


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}

