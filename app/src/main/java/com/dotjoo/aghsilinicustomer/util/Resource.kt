package com.dotjoo.aghsilinicustomer.util

import java.io.Serializable

//sealed class Resource<T> {
    sealed class Resource<T>(val data: T ?= null, open val message: String? = null): Serializable {
        data class Progress<T>(val loading: Boolean ) : Resource<T>()
 //   data class Success<T>(val data: T) : Resource<T>()
 //   class Success<T>(val data: T): Resource<T>( )
   // class Success<T>(val data: T): Resource<T>( ), Serializable
    class Success<T>(data: T): Resource<T>(data), Serializable

    data class Failure<T>(override val message: String? ) : Resource<T>()

    companion object {
        fun <T> loading(isLoading: Boolean = true, partialData: T? = null): Resource<T> =
            Progress(isLoading )

        fun <T> success(data: T): Resource<T> = Success(data)

        fun <T> failure(e: String? ): Resource<T> = Failure(e  )
    }
}
