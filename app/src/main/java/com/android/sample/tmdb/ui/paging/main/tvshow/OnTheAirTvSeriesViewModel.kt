package com.android.sample.tmdb.ui.paging.main.tvshow

import com.android.sample.tmdb.domain.model.TVShow
import com.android.sample.tmdb.repository.OnTheAirTvSeriesPagingRepository
import com.android.sample.tmdb.ui.paging.main.BaseMainPagingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnTheAirTvSeriesViewModel @Inject constructor(repository: OnTheAirTvSeriesPagingRepository) :
    BaseMainPagingViewModel<TVShow>(repository)