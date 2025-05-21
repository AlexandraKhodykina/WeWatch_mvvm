package com.sample.wewatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.wewatch.model.MovieRepository

class AddMovieViewModelFactory (private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddMovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddMovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}