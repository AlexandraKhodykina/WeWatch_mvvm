package com.sample.wewatch.model

import androidx.lifecycle.LiveData

class MovieRepository (
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource? = null
) {
    // 1. Возвращаем LiveData из LocalDataSource
    fun getAllMovies(): LiveData<List<Movie>> = localDataSource.getAllMovies()

    suspend fun insertMovie(movie: Movie) {
        localDataSource.insert(movie)
    }

    // 2. Добавляем метод для удаления
    suspend fun deleteMovie(movie: Movie) {
        localDataSource.delete(movie)
    }

    // 3. нужно работать с API (опционально)
    suspend fun getMoviesFromApi(query: String): List<Movie> {
        return remoteDataSource?.searchMovies(query) ?: emptyList()
    }
}