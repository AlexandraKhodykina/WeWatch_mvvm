package com.sample.wewatch.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val API_KEY = "1dbc7755"
    const val BASE_URL = "https://www.omdbapi.com/"
    const val IMAGE_URL = ""
    //const val TMDB_IMAGEURL = "https://m.media-amazon.com/images/M/"
/*
  const val API_KEY = "e764a27cb17b01f54152a69437559e46"
  const val TMDB_BASE_URL = "http://api.themoviedb.org/3/"
  const val TMDB_IMAGEURL = "https://image.tmdb.org/t/p/w500/"
*/
    val moviesApi: RetrofitInterface by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)
    }
}




















