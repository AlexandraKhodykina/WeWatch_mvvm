package com.sample.wewatch.model

import androidx.lifecycle.LiveData

class MovieRepository (
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource? = null // Опционально
) {
    // 1. Возвращаем LiveData из LocalDataSource
    fun getAllMovies(): LiveData<List<Movie>> = localDataSource.getAllMovies()

    // 2. Добавляем метод для удаления
    suspend fun deleteMovie(movie: Movie) {
        localDataSource.delete(movie)
    }

    // 3. Если нужно работать с API (опционально)
    suspend fun getMoviesFromApi(): List<Movie> {
        return remoteDataSource?.searchMovies("") ?: emptyList()
    }
}