package com.sample.wewatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.wewatch.model.Movie
import com.sample.wewatch.model.MovieRepository
import kotlinx.coroutines.launch
import java.util.UUID

class AddMovieViewModel (private val repository: MovieRepository) : ViewModel() {

    private val _movieData = MutableLiveData<Movie?>()
    val movieData: LiveData<Movie?> = _movieData

    private val _addMovieResult = MutableLiveData<Boolean>()
    val addMovieResult: LiveData<Boolean> = _addMovieResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setMovieData(title: String, releaseDate: String, posterPath: String, imdbID: String) {
        _movieData.value = Movie(
            imdbID = imdbID,
            title = title,
            releaseDate = releaseDate,
            posterPath = posterPath,
            overview = ""
        )
    }

    fun addMovie(title: String, releaseDate: String, posterPath: String, imdbID: String = "temp_${UUID.randomUUID()}") {
        viewModelScope.launch {
            try {
                val movie = Movie(
                    imdbID = imdbID,
                    title = title,
                    releaseDate = releaseDate,
                    posterPath = posterPath,
                    overview = ""
                )
                repository.addMovie(movie)
                _addMovieResult.postValue(true)
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to add movie: ${e.message}")
                _addMovieResult.postValue(false)
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}










