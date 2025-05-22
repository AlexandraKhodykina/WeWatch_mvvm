package com.sample.wewatch.model

import androidx.lifecycle.LiveData

class MovieRepository (
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    val movies: LiveData<List<Movie>> = localDataSource.getAllMovies()

    suspend fun addMovie(movie: Movie) {
        localDataSource.insert(movie)
    }

    // 2. Добавляем метод для удаления
    suspend fun deleteMovie(movie: Movie) {
        localDataSource.delete(movie)
    }
    suspend fun deleteMovies(movies: List<Movie>) {
        movies.forEach { localDataSource.delete(it) }
    }

    // 3. нужно работать с API (опционально)
    suspend fun getMoviesFromApi(query: String): Result<List<Movie>> {
        return remoteDataSource.searchMovies(query)
    }
}