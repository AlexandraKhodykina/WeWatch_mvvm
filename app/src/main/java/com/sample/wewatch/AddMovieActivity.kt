package com.sample.wewatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sample.wewatch.model.LocalDataSource
import com.sample.wewatch.model.Movie
import androidx.activity.viewModels
import com.sample.wewatch.model.MovieRepository
//import com.sample.wewatch.network.RetrofitClient.TMDB_IMAGEURL
import com.squareup.picasso.Picasso

open class AddMovieActivity : AppCompatActivity() {
  private lateinit var titleEditText: EditText
  private lateinit var releaseDateEditText: EditText
  private lateinit var movieImageView: ImageView
  //private lateinit var dataSource: LocalDataSource
  private val viewModel: AddMovieViewModel by viewModels {
    AddMovieViewModelFactory(MovieRepository(LocalDataSource(application)))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_movie)
    setupViews()
    //dataSource = LocalDataSource(application)
    setupObservers()
  }

  private fun setupViews() {
    titleEditText = findViewById(R.id.movie_title)
    releaseDateEditText = findViewById(R.id.movie_release_date)
    movieImageView = findViewById(R.id.movie_imageview)
  }

  private fun setupObservers() {
    // Наблюдение за данными фильма из поиска
    viewModel.movieData.observe(this) { movie ->
      movie?.let {
        titleEditText.setText(it.title)
        releaseDateEditText.setText(it.releaseDate)
        movieImageView.tag = it.posterPath
        if (it.posterPath?.isNotEmpty() == true && it.posterPath != "N/A") {
          Picasso.get().load(RetrofitClient.IMAGE_URL + it.posterPath).into(movieImageView)
        }
      }
    }

    // Наблюдение за результатом добавления
    viewModel.addMovieResult.observe(this) { success ->
      if (success) {
        setResult(Activity.RESULT_OK)
        finish()
      } else {
        showToast("Movie could not be added.")
      }
    }

    // Наблюдение за ошибками
    viewModel.errorMessage.observe(this) { error ->
      error?.let {
        showToast(it)
        viewModel.clearErrorMessage()
      }
    }
  }

  fun goToSearchMovieActivity(v: View) {
    val title = titleEditText.text.toString()
    val intent = Intent(this, SearchActivity::class.java)
    intent.putExtra(SearchActivity.SEARCH_QUERY, title)
    startActivityForResult(intent, SEARCH_MOVIE_ACTIVITY_REQUEST_CODE)
  }

  fun onClickAddMovie(v: View) {
    val title = titleEditText.text.toString()
    if (title.isEmpty()) {
      showToast("Movie title cannot be empty")
      return
    }
    val releaseDate = releaseDateEditText.text.toString()
    val posterPath = movieImageView.tag?.toString() ?: ""
    viewModel.addMovie(title, releaseDate, posterPath)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == SEARCH_MOVIE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      val title = data?.getStringExtra(SearchActivity.EXTRA_TITLE) ?: ""
      val releaseDate = data?.getStringExtra(SearchActivity.EXTRA_RELEASE_DATE) ?: ""
      val posterPath = data?.getStringExtra(SearchActivity.EXTRA_POSTER_PATH) ?: ""
      viewModel.setMovieData(title, releaseDate, posterPath)
    }
  }

  private fun showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  companion object {
    const val SEARCH_MOVIE_ACTIVITY_REQUEST_CODE = 2
  }
}
