package com.sample.wewatch.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Observable

@Dao
interface MovieDao {

  @Query("SELECT * FROM movie_table")
  fun getAllMovies(): LiveData<List<Movie>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(movies: List<Movie>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(movie: Movie)

  @Query("DELETE FROM movie_table WHERE imdbID = :imdbID")
  suspend fun delete(imdbID: String)

  @Query("DELETE FROM movie_table WHERE imdbID IN (:imdbIDs)")
  suspend fun deleteMovies(imdbIDs: List<String>)

  @Query("DELETE FROM movie_table")
  suspend fun deleteAll()

  @Update
  suspend fun update(movie: Movie)

}