package com.sample.wewatch.model

import android.util.Log
import com.sample.wewatch.network.RetrofitClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

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
            val response = RetrofitClient.moviesApi
                .searchMovie(
                    apiKey = RetrofitClient.API_KEY,
                    query = query
                )

            response.Search?.map { it.toDomainModel() } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Network error: ${e.message}")
            emptyList()
        }
    }

    private fun OmdbMovie.toDomainModel(): Movie = Movie(
        title = this.Title,
        year = this.Year,
        posterUrl = if (this.Poster != "N/A") this.Poster else null
    )
}























