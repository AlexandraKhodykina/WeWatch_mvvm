package com.sample.wewatch.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmdbResponse (
  /*
  @SerializedName("page")
  @Expose
  var page: Int? = null
  */
  @SerializedName("totalResults")
  @Expose
  val totalResults: Int? = null,
  /*
  @SerializedName("total_pages")
  @Expose
  var totalPages: Int? = null
  */
  @SerializedName("Response")
  @Expose
  val response: Boolean? = false,

  @SerializedName("Search")
  @Expose
  val results: List<Movie>? = null,
  @SerializedName("Error")
  @Expose
  val error: String? = null

)













