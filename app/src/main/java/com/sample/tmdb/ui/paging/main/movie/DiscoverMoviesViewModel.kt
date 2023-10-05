package com.sample.tmdb.ui.paging.main.movie

import com.sample.tmdb.domain.model.Movie
import com.sample.tmdb.data.repository.DiscoverMoviesPagingRepository
import com.sample.tmdb.ui.paging.main.BaseMainPagingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoverMoviesViewModel @Inject constructor(repository: DiscoverMoviesPagingRepository) :
    BaseMainPagingViewModel<Movie>(repository)