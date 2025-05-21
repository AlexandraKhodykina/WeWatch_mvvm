package com.sample.wewatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.wewatch.model.Movie
import com.sample.wewatch.model.MovieRepository
import kotlinx.coroutines.launch

class AddMovieViewModel (private val repository: MovieRepository) : ViewModel() {

    private val _movieData = MutableLiveData<Movie?>()
    val movieData: LiveData<Movie?> = _movieData

    private val _addMovieResult = MutableLiveData<Boolean>()
    val addMovieResult: LiveData<Boolean> = _addMovieResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setMovieData(title: String, releaseDate: String, posterPath: String) {
        _movieData.value = Movie(title = title, releaseDate = releaseDate, posterPath = posterPath)
    }

    fun addMovie(title: String, releaseDate: String, posterPath: String) {
        if (title.isEmpty()) {
            _errorMessage.value = "Movie title cannot be empty"
            return
        }
        viewModelScope.launch {
            try {
                val movie = Movie(title = title, releaseDate = releaseDate, posterPath = posterPath)
                repository.insertMovie(movie)
                _addMovieResult.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Error adding movie: ${e.message}"
                _addMovieResult.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}