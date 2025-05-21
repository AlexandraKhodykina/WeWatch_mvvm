package com.sample.wewatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.wewatch.model.MovieRepository

class MainViewModelFactory (private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}