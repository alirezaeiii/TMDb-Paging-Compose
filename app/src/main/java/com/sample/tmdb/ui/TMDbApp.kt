package com.sample.tmdb.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sample.tmdb.R
import com.sample.tmdb.domain.model.Cast
import com.sample.tmdb.domain.model.Crew
import com.sample.tmdb.ui.credit.CreditScreen
import com.sample.tmdb.ui.credit.PersonScreen
import com.sample.tmdb.ui.detail.MovieDetailScreen
import com.sample.tmdb.ui.detail.TVShowDetailScreen
import com.sample.tmdb.ui.feed.MovieFeedScreen
import com.sample.tmdb.ui.feed.TVShowFeedScreen
import com.sample.tmdb.ui.paging.*
import com.sample.tmdb.ui.paging.search.SearchMoviesScreen
import com.sample.tmdb.ui.paging.search.SearchTVSeriesScreen

@Composable
fun TMDbApp() {
    val appState = rememberTMDbkAppState()
    Scaffold(
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                TMDbBottomBar(
                    tabs = appState.bottomBarTabs,
                    currentRoute = appState.currentRoute!!,
                    navigateToRoute = appState::navigateToBottomBarRoute,
                )
            }
        }
    ) { innerPaddingModifier ->
        NavHost(
            navController = appState.navController,
            startDestination = MainDestinations.HOME_ROUTE,
            modifier = Modifier.padding(innerPaddingModifier)
        ) {
            tmdbNavGraph(
                navController = appState.navController,
                onMovieSelected = appState::navigateToMovieDetail,
                onTVShowSelected = appState::navigateToTVShowDetail,
                onAllCastSelected = appState::navigateToCastList,
                onAllCrewSelected = appState::navigateToCrewList,
                onCreditSelected = appState::navigateToPerson,
                upPress = appState::upPress
            )
        }
    }
}

@Composable
private fun TMDbBottomBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit
) {
    val currentSection = tabs.first { it.route == currentRoute }

    Box(
        Modifier
            .background(MaterialTheme.colors.background)
            .navigationBarsPadding()
    ) {
        BottomNavigation {
            tabs.forEach { section ->
                val selected = section == currentSection
                BottomNavigationItem(
                    label = {
                        Text(text = section.title)
                    },
                    icon = {
                        Icon(
                            painterResource(id = section.icon),
                            contentDescription = section.title
                        )
                    },
                    selected = selected,
                    unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                    onClick = { navigateToRoute(section.route) }
                )
            }
        }
    }
}

private fun NavGraphBuilder.tmdbNavGraph(
    navController: NavController,
    onMovieSelected: (Int, NavBackStackEntry) -> Unit,
    onTVShowSelected: (Int, NavBackStackEntry) -> Unit,
    onAllCastSelected: (String, NavBackStackEntry) -> Unit,
    onAllCrewSelected: (String, NavBackStackEntry) -> Unit,
    onCreditSelected: (String, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.MOVIE_SECTION.route
    ) {
        composable(route = HomeSections.MOVIE_SECTION.route) { from ->
            MovieFeedScreen(navController = navController, onClick = {
                onMovieSelected(it.id, from)
            })
        }
        composable(route = HomeSections.TV_SHOW_SECTION.route) { from ->
            TVShowFeedScreen(navController = navController, onClick = {
                onTVShowSelected(it.id, from)
            })
        }
    }
    composable(
        route = "${MainDestinations.TMDB_MOVIE_DETAIL_ROUTE}/{${MainDestinations.TMDB_ID_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.TMDB_ID_KEY) { type = NavType.IntType })
    ) { from ->
        MovieDetailScreen(upPress, onAllCastSelected = {
            onAllCastSelected(
                Uri.encode(gson.toJson(it, object : TypeToken<List<Cast>>() {}.type)),
                from
            )
        }, onAllCrewSelected = {
            onAllCrewSelected(
                Uri.encode(gson.toJson(it, object : TypeToken<List<Crew>>() {}.type)),
                from
            )
        }, onCreditSelected = {
            onCreditSelected(it, from)
        })
    }
    composable(
        route = "${MainDestinations.TMDB_TV_SHOW_DETAIL_ROUTE}/{${MainDestinations.TMDB_ID_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.TMDB_ID_KEY) { type = NavType.IntType })
    ) { from ->
        TVShowDetailScreen(upPress, onAllCastSelected = {
            onAllCastSelected(
                Uri.encode(gson.toJson(it, object : TypeToken<List<Cast>>() {}.type)),
                from
            )
        }, onAllCrewSelected = {
            onAllCrewSelected(
                Uri.encode(gson.toJson(it, object : TypeToken<List<Crew>>() {}.type)),
                from
            )
        }, onCreditSelected = {
            onCreditSelected(it, from)
        })
    }
    composable(route = MainDestinations.TMDB_TRENDING_MOVIES_ROUTE) { from ->
        TrendingMovieScreen(
            onClick = { onMovieSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_POPULAR_MOVIES_ROUTE) { from ->
        PopularMovieScreen(
            onClick = { onMovieSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_NOW_PLAYING_MOVIES_ROUTE) { from ->
        NowPlayingMovieScreen(
            onClick = { onMovieSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_UPCOMING_MOVIES_ROUTE) { from ->
        UpcomingMovieScreen(
            onClick = { onMovieSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_TOP_RATED_MOVIES_ROUTE) { from ->
        TopRatedMovieScreen(
            onClick = { onMovieSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_TRENDING_TV_SHOW_ROUTE) { from ->
        TrendingTVShowScreen(
            onClick = { onTVShowSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_POPULAR_TV_SHOW_ROUTE) { from ->
        PopularTVShowScreen(
            onClick = { onTVShowSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_AIRING_TODAY_TV_SHOW_ROUTE) { from ->
        AiringTodayTVShowScreen(
            onClick = { onTVShowSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_ON_THE_AIR_TV_SHOW_ROUTE) { from ->
        OnTheAirTVShowScreen(
            onClick = { onTVShowSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_TOP_RATED_TV_SHOW_ROUTE) { from ->
        TopRatedTVShowScreen(
            onClick = { onTVShowSelected(it.id, from) },
            upPress = upPress,
            navController = navController
        )
    }
    composable(route = MainDestinations.TMDB_SEARCH_MOVIE_ROUTE) { from ->
        SearchMoviesScreen(onClick = { onMovieSelected(it.id, from) }, upPress = upPress)
    }
    composable(route = MainDestinations.TMDB_SEARCH_TV_SHOW_ROUTE) { from ->
        SearchTVSeriesScreen(onClick = { onTVShowSelected(it.id, from) }, upPress = upPress)
    }
    composable(
        route = "${MainDestinations.TMDB_CAST_ROUTE}/{${MainDestinations.TMDB_CREDIT_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.TMDB_CREDIT_KEY) { type = NavType.StringType })
    ) { from ->
        CreditScreen(
            R.string.cast,
            onCreditSelected = {
                onCreditSelected(it, from)
            },
            upPress,
            gson.fromJson<List<Cast>>(
                from.arguments?.getString(MainDestinations.TMDB_CREDIT_KEY),
                object : TypeToken<List<Cast>>() {}.type
            )
        )
    }
    composable(
        route = "${MainDestinations.TMDB_CREW_ROUTE}/{${MainDestinations.TMDB_CREDIT_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.TMDB_CREDIT_KEY) { type = NavType.StringType })
    ) { from ->
        CreditScreen(
            R.string.crew,
            onCreditSelected = {
                onCreditSelected(it, from)
            },
            upPress,
            gson.fromJson<List<Crew>>(
                from.arguments?.getString(MainDestinations.TMDB_CREDIT_KEY),
                object : TypeToken<List<Crew>>() {}.type
            )
        )
    }
    composable(
        route = "${MainDestinations.TMDB_PERSON_ROUTE}/{${MainDestinations.TMDB_PERSON_KEY}}",
        arguments = listOf(
            navArgument(MainDestinations.TMDB_PERSON_KEY) { type = NavType.StringType })
    ) {
        PersonScreen(upPress)
    }
}

enum class HomeSections(val route: String, val title: String, val icon: Int) {
    MOVIE_SECTION("Movie", "Movie", R.drawable.ic_movie),
    TV_SHOW_SECTION("TVShow", "TVShow", R.drawable.ic_tv_series)
}

private val gson = Gson()