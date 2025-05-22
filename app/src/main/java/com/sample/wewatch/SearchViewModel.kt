package com.sample.wewatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.wewatch.model.Movie
import com.sample.wewatch.model.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel (private val repository: MovieRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _selectedMovie = MutableLiveData<Movie?>()
    val selectedMovie: LiveData<Movie?> = _selectedMovie

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = repository.getMoviesFromApi(query)
                _searchResults.value = movies
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching movies: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onMovieSelected(movie: Movie) {
        _selectedMovie.value = movie
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}