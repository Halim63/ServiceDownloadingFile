package com.halim.downloadfile.base

class Resource<T> private constructor(
    val state: State,
    val result: T? = null,
    val errorMessage: String? = null,
) {
    companion object {
        fun <T> success(result: T): Resource<T> = Resource(
            state = State.SUCCESS, result = result
        )

        fun <T> error(message: String?): Resource<T> = Resource(
            state = State.ERROR, errorMessage = message
        )

        fun <T> loading(): Resource<T> = Resource(state = State.LOADING)
    }
}

enum class State {
    LOADING, SUCCESS, ERROR,
}