package com.halim.downloadfile.repository.books.remote

import javax.inject.Inject

class BooksRemoteRepo @Inject constructor(
    private val booksApiRetrofitInterface: BooksApiRetrofitInterface,
) {
     fun downloadBook(bookUrl: String) =
        booksApiRetrofitInterface.downloadBook(bookUrl = bookUrl)
     fun multipleDownload() =
        booksApiRetrofitInterface.multipleDownload()



}

