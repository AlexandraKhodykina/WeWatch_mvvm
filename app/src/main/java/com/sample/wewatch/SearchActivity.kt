package com.sample.wewatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.wewatch.model.RemoteDataSource
import androidx.activity.viewModels
import com.sample.wewatch.model.LocalDataSource
import com.sample.wewatch.model.Movie
import com.sample.wewatch.model.MovieRepository
import com.sample.wewatch.model.TmdbResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import androidx.activity.viewModels

import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

//const val SEARCH_QUERY = "searchQuery"

class SearchActivity : AppCompatActivity() {
  //private val TAG = "SearchActivity"
  private lateinit var searchResultsRecyclerView: RecyclerView
  private lateinit var adapter: SearchAdapter
  private lateinit var noMoviesTextView: TextView
  private lateinit var progressBar: ProgressBar
  private var query = ""

  private val viewModel: SearchViewModel by viewModels {
    SearchViewModelFactory(MovieRepository(LocalDataSource(application), RemoteDataSource()))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search_movie)
    query = intent.getStringExtra(SEARCH_QUERY) ?: ""
    setupViews()
    setupObservers()
    viewModel.searchMovies(query)
  }


private fun setupViews() {
  searchResultsRecyclerView = findViewById(R.id.search_results_recyclerview)
  searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
  adapter = SearchAdapter(emptyList(), this) { movie ->
    viewModel.onMovieSelected(movie)
  }
  searchResultsRecyclerView.adapter = adapter
  noMoviesTextView = findViewById(R.id.no_movies_textview)
  progressBar = findViewById(R.id.progress_bar)
}

private fun setupObservers() {
  viewModel.searchResults.observe(this) { movies ->
    if (movies.isEmpty()) {
      searchResultsRecyclerView.visibility = View.INVISIBLE
      noMoviesTextView.visibility = View.VISIBLE
    } else {
      adapter.updateMovies(movies)
      searchResultsRecyclerView.visibility = View.VISIBLE
      noMoviesTextView.visibility = View.INVISIBLE
    }
  }

  viewModel.isLoading.observe(this) { isLoading ->
    progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
  }

  viewModel.errorMessage.observe(this) { error ->
    error?.let {
      showToast(it)
      viewModel.clearErrorMessage()
    }
  }

  viewModel.selectedMovie.observe(this) { movie ->
    movie?.let {
      val replyIntent = Intent()
      replyIntent.putExtra(EXTRA_TITLE, it.title)
      replyIntent.putExtra(EXTRA_RELEASE_DATE, it.getReleaseYearFromDate())
      replyIntent.putExtra(EXTRA_POSTER_PATH, it.posterPath)
      replyIntent.putExtra(EXTRA_IMDB_ID, it.imdbID)
      setResult(RESULT_OK, replyIntent)
      finish()
    }
  }
}

private fun showToast(message: String) {
  Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

companion object {
  const val SEARCH_QUERY = "searchQuery"
  const val EXTRA_TITLE = "SearchActivity.TITLE_REPLY"
  const val EXTRA_RELEASE_DATE = "SearchActivity.RELEASE_DATE_REPLY"
  const val EXTRA_POSTER_PATH = "SearchActivity.POSTER_PATH_REPLY"
  const val EXTRA_IMDB_ID = "SearchActivity.IMDB_ID_REPLY"
}

}

