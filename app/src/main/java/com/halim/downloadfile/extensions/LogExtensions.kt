package com.halim.downloadfile.extensions

import android.util.Log

fun Any.debug(message: Any){
    Log.d(Any::class.java.toString(),message.toString())
}

fun Any.error(throwable:Throwable,message: Any? = null){
    Log.e(Any::class.java.toString(),message?.toString(),throwable)
}