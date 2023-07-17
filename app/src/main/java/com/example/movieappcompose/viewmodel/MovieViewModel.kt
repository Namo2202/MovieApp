package com.example.movieappcompose.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappcompose.data.Movie
import com.example.movieappcompose.data.MovieResponse
import com.example.movieappcompose.data.TMDBApi
import com.example.movieappcompose.data.Trailer
import com.example.movieappcompose.util.ApiKeyProvider.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieViewModel : ViewModel() {
    private val apiService = TMDBApi.create()

    // LiveData for popular movies
    val popularMovies = MutableLiveData<List<Movie>>()

    // LiveData for top rated movies
    val topRatedMovies = MutableLiveData<List<Movie>>()

    // LiveData for upcoming movies
    val upcomingMovies = MutableLiveData<List<Movie>>()

    // LiveData for now playing movies
    val nowPlayingMovies = MutableLiveData<List<Movie>>()

    // LiveData for search results
    val searchResults = MutableLiveData<List<Movie>>()

    // Fetch popular movies
    fun fetchPopularMovies() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                apiService.getPopularMovies()
            }
            popularMovies.value = response.results
        }
    }

    // Fetch top rated movies
    fun fetchTopRatedMovies() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                apiService.getTopRatedMovies()
            }
            topRatedMovies.value = response.results
        }
    }

    // Fetch upcoming movies
    fun fetchUpcomingMovies() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                apiService.getUpcomingMovies()
            }
            upcomingMovies.value = response.results
        }
    }

    // Fetch now playing movies
    fun fetchNowPlayingMovies() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                apiService.getNowPlayingMovies()
            }
            nowPlayingMovies.value = response.results
        }
    }

    // Search movies
    fun searchMovies(searchText: String) {
        viewModelScope.launch {
            if (searchText.isNotEmpty()) {
                val response = withContext(Dispatchers.IO) {
                    apiService.searchMovies(API_KEY, searchText)
                }
                searchResults.value = response.results
            } else {
                searchResults.value = emptyList()
            }
        }
    }

    suspend fun fetchMovieDetails(movieId: String): Movie {
        return withContext(Dispatchers.IO) {
            apiService.getMovieDetails(movieId)
        }
    }

    suspend fun fetchMovieTrailers(movieId: String): List<Trailer> {
        return withContext(Dispatchers.IO) {
            apiService.getMovieTrailers(movieId).results
        }
    }

}
