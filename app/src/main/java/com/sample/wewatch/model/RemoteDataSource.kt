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
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Searching movies for query: $query")
                val response = RetrofitClient.moviesApi.searchMovie(
                    apiKey = RetrofitClient.API_KEY,
                    query = query
                ).body()
                response?.results?.map { it.toDomainModel() } ?: emptyList()
            } catch (e: Exception) {
                Log.e(TAG, "Network error: ${e.message}")
                emptyList()
            }
        }
    }
}

private fun Movie.toDomainModel(): Movie {
    return Movie(
        title = this.title,
        releaseDate = this.releaseDate,
        posterPath = this.posterPath,
        overview = this.overview,
        imdbID = this.imdbID
    )
}























