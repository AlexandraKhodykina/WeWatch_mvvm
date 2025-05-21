package com.sample.wewatch.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val API_KEY = "1dbc7755" // Вынесите в BuildConfig на проде!
    const val BASE_URL = "https://www.omdbapi.com/"
    const val IMAGE_URL = ""
    //const val TMDB_IMAGEURL = "https://m.media-amazon.com/images/M/"
/*
  const val API_KEY = "e764a27cb17b01f54152a69437559e46"
  const val TMDB_BASE_URL = "http://api.themoviedb.org/3/"
  const val TMDB_IMAGEURL = "https://image.tmdb.org/t/p/w500/"
*/
    val moviesApi: RetrofitInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // Убираем RxJava-адаптер, т.к. переходим на корутины
            .build()
            .create(RetrofitInterface::class.java)
    }
}




















