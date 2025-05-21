package com.sample.wewatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sample.wewatch.model.LocalDataSource
import com.sample.wewatch.model.Movie
import androidx.activity.viewModels
//viewModels???
import com.sample.wewatch.model.MovieRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

  private lateinit var moviesRecyclerView: RecyclerView
  private lateinit var adapter: MainAdapter
  private lateinit var fab: FloatingActionButton
  private lateinit var noMoviesLayout: LinearLayout

  private val viewModel: MainViewModel by viewModels {
    MainViewModelFactory(MovieRepository(LocalDataSource(application)))
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setupViews()
    setupObservers()
  }

  private fun setupViews() {
    moviesRecyclerView = findViewById(R.id.movies_recyclerview)
    moviesRecyclerView.layoutManager = LinearLayoutManager(this)
    adapter = MainAdapter(emptyList(), this) { movie ->
      viewModel.deleteMovie(movie)
    }
    moviesRecyclerView.adapter = adapter
    fab = findViewById(R.id.fab)
    noMoviesLayout = findViewById(R.id.no_movies_layout)
    supportActionBar?.title = "Movies to Watch"
  }

  private fun setupObservers() {
    // Наблюдение за списком фильмов
    viewModel.movies.observe(this) { movies ->
      displayMovies(movies)
    }

    // Наблюдение за ошибками
    viewModel.errorMessage.observe(this) { error ->
      error?.let {
        showToast(it)
        viewModel.clearErrorMessage()
      }
    }

    // Наблюдение за состоянием загрузки
    viewModel.isLoading.observe(this) { isLoading ->
      // Можно добавить ProgressBar в layout и показывать/скрывать его
    }
  }

  private fun displayMovies(movieList: List<Movie>?) {
    if (movieList.isNullOrEmpty()) {
      moviesRecyclerView.visibility = View.INVISIBLE
      noMoviesLayout.visibility = View.VISIBLE
    } else {
      adapter.updateMovies(movieList)
      moviesRecyclerView.visibility = View.VISIBLE
      noMoviesLayout.visibility = View.INVISIBLE
    }
  }

  fun goToAddMovieActivity(v: View) {
    val intent = Intent(this, AddMovieActivity::class.java)
    startActivityForResult(intent, ADD_MOVIE_ACTIVITY_REQUEST_CODE)
  }

  override fun onActivityResult(requestCode disposing: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == ADD_MOVIE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
      showToast("Movie successfully added.")
    } else {
      showToast("Movie could not be added.")
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.deleteMenuItem) {
      adapter.getSelectedMovies().forEach { movie ->
        viewModel.deleteMovie(movie)
      }
      val count = adapter.getSelectedMovies().size
      if (count == 1) {
        showToast("Movie deleted")
      } else if (count > 1) {
        showToast("Movies deleted")
      }
      adapter.clearSelection()
    }
    return super.onOptionsItemSelected(item)
  }

  private fun showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  companion object {
    const val ADD_MOVIE_ACTIVITY_REQUEST_CODE = 1
  }

}