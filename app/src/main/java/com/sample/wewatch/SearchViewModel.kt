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
            _isLoading.value = true
            val result = repository.getMoviesFromApi(query)
            _isLoading.value = false
            when {
                result.isSuccess -> {
                    _searchResults.value = result.getOrDefault(emptyList())
                }
                result.isFailure -> {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Error fetching movies"
                    _searchResults.value = emptyList()
                }
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