package com.sample.wewatch.model

import android.util.Log
import com.sample.wewatch.network.RetrofitClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import retrofit2.HttpException
import java.io.IOException
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
    suspend fun searchMovies(query: String): Result<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Searching movies for query: $query")
                val response = RetrofitClient.moviesApi.searchMovie(
                    apiKey = RetrofitClient.API_KEY,
                    query = query
                )
                Log.d(TAG, "HTTP Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d(TAG, "Response Body: $body")
                    if (body?.response == true && body.results != null) {
                        Result.success(body.results.mapNotNull { it.toDomainModel() })
                    } else {
                        Result.failure(Exception(body?.error ?: "No movies found"))
                    }
                } else {
                    Log.e(TAG, "HTTP Error: ${response.message()}, Code: ${response.code()}")
                    Result.failure(HttpException(response))
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error: ${e.message}")
                Result.failure(e)
            } catch (e: HttpException) {
                Log.e(TAG, "HTTP error: ${e.message()}, Code: ${e.code()}")
                Result.failure(e)
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error: ${e.message}")
                Result.failure(e)
            }
        }
    }
}

private fun Movie.toDomainModel(): Movie? {
    val imdbID = this.imdbID
    return if (!imdbID.isNullOrEmpty()) {
        Movie(
            imdbID = imdbID,
            title = this.title,
            releaseDate = this.releaseDate,
            posterPath = this.posterPath,
            overview = this.overview,
            watched = this.watched
        )
    } else {
        Log.w("RemoteDataSource", "Skipping movie with null or empty imdbID: $this")
        null
    }
}























