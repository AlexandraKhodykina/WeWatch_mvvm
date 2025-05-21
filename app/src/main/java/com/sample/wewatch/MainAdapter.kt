package com.sample.wewatch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.wewatch.model.Movie
import com.sample.wewatch.network.RetrofitClient
import com.squareup.picasso.Picasso
import java.util.HashSet

class MainAdapter(
  private var movieList: List<Movie>,
  private val context: Context,
  private val onDeleteClick: (Movie) -> Unit
) : RecyclerView.Adapter<MainAdapter.MoviesHolder>() {

  private val selectedMovies = mutableSetOf<Movie>()

  fun updateMovies(newMovies: List<Movie>) {
    movieList = newMovies
    notifyDataSetChanged()
  }

  fun getSelectedMovies(): Set<Movie> = selectedMovies

  fun clearSelection() {
    selectedMovies.clear()
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
    val v = LayoutInflater.from(context).inflate(R.layout.item_movie_main, parent, false)
    return MoviesHolder(v)
  }

  override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
    val movie = movieList[position]
    holder.titleTextView.text = movie.title
    holder.releaseDateTextView.text = movie.releaseDate
    if (movie.posterPath.isEmpty()) {
      holder.movieImageView.setImageDrawable(context.getDrawable(R.drawable.ic_local_movies_gray))
    } else {
      Picasso.get().load(RetrofitClient.TMDB_IMAGEURL + movie.posterPath).into(holder.movieImageView)
    }
    holder.checkBox.isChecked = selectedMovies.contains(movie)
    holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        selectedMovies.add(movie)
      } else {
        selectedMovies.remove(movie)
      }
    }
  }

  override fun getItemCount(): Int = movieList.size

  inner class MoviesHolder(v: View) : RecyclerView.ViewHolder(v) {
    val titleTextView: TextView = v.findViewById(R.id.title_textview)
    val releaseDateTextView: TextView = v.findViewById(R.id.release_date_textview)
    val movieImageView: ImageView = v.findViewById(R.id.movie_imageview)
    val checkBox: CheckBox = v.findViewById(R.id.checkbox)
  }
}