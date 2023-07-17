package com.example.movieappcompose.home


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.movieappcompose.data.Movie
import com.example.movieappcompose.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@Composable
fun HomeScreen(navController: NavController, viewModel: MovieViewModel) {

    LaunchedEffect(Unit) {
        viewModel.fetchPopularMovies()
        viewModel.fetchTopRatedMovies()
        viewModel.fetchUpcomingMovies()
        viewModel.fetchNowPlayingMovies()
    }

    val popularMovies by viewModel.popularMovies.observeAsState(emptyList())
    val topRatedMovies by viewModel.topRatedMovies.observeAsState(emptyList())
    val upcomingMovies by viewModel.upcomingMovies.observeAsState(emptyList())
    val nowPlayingMovies by viewModel.nowPlayingMovies.observeAsState(emptyList())


    var searchText by remember { mutableStateOf("") }
    var isSearchBarVisible by remember { mutableStateOf(false) }

    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            viewModel.searchMovies(searchText)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("favorites") },
                    contentPadding = PaddingValues(start = 8.dp, end = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = Color.Yellow
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Favorites", color = MaterialTheme.colorScheme.onBackground)
                    }
                }

                AnimatedVisibility(
                    visible = isSearchBarVisible,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            label = { Text("Search") },
                            modifier = Modifier.weight(1f).padding(start = 16.dp)
                        )

                        IconButton(
                            onClick = {
                                isSearchBarVisible = !isSearchBarVisible
                                searchText = ""
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Red
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { isSearchBarVisible = !isSearchBarVisible }
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Red
                    )
                }
            }
        }
        if (searchText.isEmpty()) {
            item {
                MovieCategoryRow("Popular Movies", popularMovies, navController)
            }
            item {
                MovieCategoryRow("Top Rated Movies", topRatedMovies, navController)
            }
            item {
                MovieCategoryRow("Upcoming Movies", upcomingMovies, navController)
            }
            item {
                MovieCategoryRow("Now Playing Movies", nowPlayingMovies, navController)
            }
        } else {
            item {
                viewModel.searchResults.value?.let {
                    MovieCategoryRow("Search Results",
                        it, navController)
                }
            }

        }

    }
}

@Composable
fun MovieCategoryRow(categoryTitle: String, movies: List<Movie>, navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = categoryTitle, style = MaterialTheme.typography.titleMedium, color = Color.Red)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(start = 8.dp, end = 16.dp)) {
            items(movies) { movie ->
                MovieItem(movie, navController)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navController: NavController) {
    val imageSize = "w500"
    val fullImageUrl = "https://image.tmdb.org/t/p/$imageSize/${movie.poster_path}"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { navController.navigate("details/${movie.id}") },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = rememberImagePainter(fullImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillBounds
            )
        }
    }

        Text(
            text = movie.title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
}
}
