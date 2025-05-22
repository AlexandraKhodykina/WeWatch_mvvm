package com.sample.wewatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.wewatch.model.Movie
import com.sample.wewatch.model.MovieRepository
import kotlinx.coroutines.launch

class MainViewModel (private val repository: MovieRepository) : ViewModel() {

    val movies: LiveData<List<Movie>> = repository.movies

    private val _navigateToAddMovie = MutableLiveData<Boolean>()
    val navigateToAddMovie: LiveData<Boolean> = _navigateToAddMovie

    private val _selectedMovies = MutableLiveData<Set<Movie>>(emptySet())
    val selectedMovies: LiveData<Set<Movie>> = _selectedMovies

    fun onAddMovieClicked() {
        _navigateToAddMovie.value = true
    }

    fun onAddMovieNavigated() {
        _navigateToAddMovie.value = false
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            repository.deleteMovie(movie)
        }
    }

    fun deleteMovies(movies: List<Movie>) {
        viewModelScope.launch {
            repository.deleteMovies(movies)
        }
    }

    fun toggleMovieSelection(movie: Movie) {
        val currentSelection = _selectedMovies.value?.toMutableSet() ?: mutableSetOf()
        if (currentSelection.contains(movie)) {
            currentSelection.remove(movie)
        } else {
            currentSelection.add(movie)
        }
        _selectedMovies.value = currentSelection
    }

    fun clearSelection() {
        _selectedMovies.value = emptySet()
    }
}
































