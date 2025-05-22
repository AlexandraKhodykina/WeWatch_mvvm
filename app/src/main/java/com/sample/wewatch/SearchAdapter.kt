package com.sample.wewatch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.wewatch.model.Movie
import com.squareup.picasso.Picasso
import com.sample.wewatch.network.RetrofitClient


class SearchAdapter(
  private var movieList: List<Movie>,
  private val context: Context,
  private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchMoviesHolder>() {

  fun updateMovies(newMovies: List<Movie>) {
    movieList = newMovies
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMoviesHolder {
    val view = LayoutInflater.from(context).inflate(R.layout.item_movie_details, parent, false)
    return SearchMoviesHolder(view)
  }

  override fun onBindViewHolder(holder: SearchMoviesHolder, position: Int) {
    val movie = movieList[position]
    holder.titleTextView.text = movie.title
    holder.releaseDateTextView.text = movie.getReleaseYearFromDate()
    holder.overviewTextView.text = movie.overview
    if (movie.posterPath != "N/A" && movie.posterPath != null) {
      Picasso.get().load(RetrofitClient.IMAGE_URL + movie.posterPath).into(holder.movieImageView)
    } else {
      holder.movieImageView.setImageDrawable(context.getDrawable(R.drawable.ic_local_movies_gray))
    }
    holder.itemView.setOnClickListener {
      onItemClick(movie)
    }
  }

  override fun getItemCount(): Int = movieList.size

  inner class SearchMoviesHolder(v: View) : RecyclerView.ViewHolder(v) {
    val titleTextView: TextView = v.findViewById(R.id.title_textview)
    val overviewTextView: TextView = v.findViewById(R.id.overview_overview)
    val releaseDateTextView: TextView = v.findViewById(R.id.release_date_textview)
    val movieImageView: ImageView = v.findViewById(R.id.movie_imageview)
  }
}
