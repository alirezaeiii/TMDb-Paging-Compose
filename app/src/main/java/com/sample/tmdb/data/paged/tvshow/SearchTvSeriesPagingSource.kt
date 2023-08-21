package com.sample.tmdb.data.paged.tvshow

import android.content.Context
import com.sample.tmdb.data.network.TVShowService
import com.sample.tmdb.data.paged.BasePagingSource
import com.sample.tmdb.data.response.asTVShowDomainModel
import com.sample.tmdb.domain.model.TVShow

class SearchTvSeriesPagingSource(
    context: Context,
    private val tvShowApi: TVShowService,
    private val query: String
) : BasePagingSource<TVShow>(context) {

    override suspend fun fetchItems(page: Int): List<TVShow> =
        tvShowApi.searchTVSeries(page, query).items.asTVShowDomainModel()
}