package com.example.movieappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieappcompose.data.Movie
import com.example.movieappcompose.detail.DetailsScreen
import com.example.movieappcompose.favorite.FavoritesScreen
import com.example.movieappcompose.home.HomeScreen
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.movieappcompose.viewmodel.MovieViewModel
import com.example.movieappcompose.ui.theme.MovieAppComposeTheme

class MainActivity : ComponentActivity() {
    private val movieViewModel: MovieViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppComposeTheme {
                val navController: NavHostController = rememberNavController()

                val favorites = remember { mutableStateOf(emptyList<Movie>()) }

                Surface(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.onBackground)) {

                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController, movieViewModel)
                    }
                    composable("details/{movieId}") { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getString("movieId")
                        DetailsScreen(navController, movieId, favorites, movieViewModel)
                    }
                    composable("favorites") {
                        FavoritesScreen(navController, favorites)
                    }
                }
            }
            }
        }
    }
}