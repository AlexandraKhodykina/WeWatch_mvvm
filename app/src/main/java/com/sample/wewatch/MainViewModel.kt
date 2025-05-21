package com.sample.wewatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.wewatch.model.Movie
import com.sample.wewatch.model.MovieRepository
import kotlinx.coroutines.launch

class MainViewModel (private val repository: MovieRepository) : ViewModel() {

    val movies: LiveData<List<Movie>> = repository.getAllMovies()

    // Состояния UI
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun refreshMovies() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Если нужно принудительно обновить данные (например, из API)
                // repository.refreshMoviesFromApi()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка обновления: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteMovie(movie)
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка удаления: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}

































