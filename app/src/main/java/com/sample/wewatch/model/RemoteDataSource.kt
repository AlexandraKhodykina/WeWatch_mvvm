package com.sample.wewatch.model

import android.util.Log
import com.sample.wewatch.network.RetrofitClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.schedulers.Schedulers

open class RemoteDataSource {
    private val TAG = "RemoteDataSource"

  //fun searchResultsObservable(query: String): Observable<TmdbResponse> {
      //Log.d(TAG, "search/movie")
    //return RetrofitClient.moviesApi
     //   .searchMovie(RetrofitClient.API_KEY, query)
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(AndroidSchedulers.mainThread())
  //}
  // 1. Заменяем Observable на suspend-функцию
  suspend fun searchMovies(query: String): List<Movie> {
      return try {
          Log.d(TAG, "Searching movies for query: $query")
          val response = RetrofitClient.moviesApi
              .searchMovie(RetrofitClient.API_KEY, query)
              .awaitResponse() // Используем await для корутин

          if (response.isSuccessful) {
              response.body()?.results ?: emptyList()
          } else {
              Log.e(TAG, "API error: ${response.code()} - ${response.message()}")
              emptyList()
          }
      } catch (e: Exception) {
          Log.e(TAG, "Network error: ${e.message}")
          emptyList()
      }
  }
}