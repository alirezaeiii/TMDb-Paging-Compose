package com.android.sample.tmdb.repository

import android.content.Context
import com.android.sample.tmdb.data.network.MovieService
import com.android.sample.tmdb.data.paged.BasePagingSource
import com.android.sample.tmdb.data.paged.movie.UpcomingMoviesPagingSource
import com.android.sample.tmdb.domain.BasePagingRepository
import com.android.sample.tmdb.domain.model.Movie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpcomingMoviesPagingRepository @Inject constructor(
    private val context: Context,
    private val movieApi: MovieService
) : BasePagingRepository<Movie>() {

    override val pagingSource: BasePagingSource<Movie>
        get() = UpcomingMoviesPagingSource(context, movieApi)
}