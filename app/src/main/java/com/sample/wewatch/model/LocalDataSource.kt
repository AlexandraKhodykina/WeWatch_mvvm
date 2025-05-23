package com.sample.wewatch.model

import android.app.Application
import androidx.room.Room
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import kotlin.concurrent.thread

open class LocalDataSource(application: Application) {

  private val movieDao: MovieDao
  //open val allMovies: Observable<List<Movie>>

  init {
    val db = LocalDatabase.getInstance(application)
    movieDao = db.movieDao()
      //allMovies = movieDao.all
  }
  // 1. Получение всех фильмов (LiveData)
  fun getAllMovies(): LiveData<List<Movie>> = movieDao.getAllMovies()

  // 2. Добавление фильма (suspend)
  suspend fun insert(movie: Movie) {
    movieDao.insert(movie)
  }
  // 3. Массовое добавление (для кэширования из API)
  suspend fun insertMovies(movies: List<Movie>) {
    movieDao.insertAll(movies)
  }
  suspend fun delete(movie: Movie) {
    movie.imdbID.let { movieDao.delete(it) }
  }
  suspend fun deleteMovies(movies: List<Movie>) {
    movieDao.deleteMovies(movies.map { it.imdbID })
  }

  // 5. Удаление всех (suspend)
  suspend fun deleteAll() {
    movieDao.deleteAll()
  }
  // 6. Обновление (suspend) - убрали thread{}!
  suspend fun update(movie: Movie) {
    movieDao.update(movie)
  }
}
















