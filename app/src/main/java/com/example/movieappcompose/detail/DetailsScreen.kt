package com.example.movieappcompose.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.movieappcompose.data.Movie
import com.example.movieappcompose.data.Trailer
import com.example.movieappcompose.viewmodel.MovieViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(navController: NavController, movieId: String?, favorites: MutableState<List<Movie>>, viewModel: MovieViewModel) {
    var movieDetails by remember { mutableStateOf<Movie?>(null) }
    var trailers by remember { mutableStateOf<List<Trailer>?>(null) }

    val scope = rememberCoroutineScope()
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        if (!movieId.isNullOrBlank()) {
            scope.launch {
                val response = viewModel.fetchMovieDetails(movieId)
                movieDetails = response

                val trailersResponse = viewModel.fetchMovieTrailers(movieId)
                trailers = trailersResponse
            }
        }
    }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {

                movieDetails?.let { movie ->
                    Text(
                        text = movie.title,
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    val imageSize = "w500"
                    val fullImageUrl = "https://image.tmdb.org/t/p/$imageSize/${movie.poster_path}"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            painter = rememberImagePainter(fullImageUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    println("overview URL: ${movie.overview ?: ""}")

                    Text(
                        text = movie.overview ?: "",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    println("vote_average URL: ${movie.vote_average}")

                    RatingBar(movie.vote_average)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 16.dp),
                            onClick = {
                                isFavorite = !isFavorite

                                if (movieDetails != null && !favorites.value.contains(movieDetails)) {
                                    favorites.value = favorites.value.toMutableList().apply {
                                        add(movieDetails!!)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Yellow else MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Favorites", color = MaterialTheme.colorScheme.onBackground)
                        }

                        trailers?.let { trailerList ->
                            if (trailerList.isNotEmpty()) {
                                val officialTrailer = trailerList.firstOrNull { it.type == "Trailer" && it.official }

                                if (officialTrailer != null) {
                                    Button(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(top = 16.dp, start = 8.dp),
                                        onClick = {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://www.youtube.com/watch?v=${officialTrailer.key}")
                                            )
                                            navController.context.startActivity(intent)
                                        }
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = MaterialTheme.colorScheme.onBackground)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Play", color = MaterialTheme.colorScheme.onBackground )
                                    }
                                } else {
                                    val defaultTrailer = trailerList.first()
                                    Button(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(top = 16.dp, start = 8.dp),
                                        onClick = {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://www.youtube.com/watch?v=${defaultTrailer.key}")
                                            )
                                            navController.context.startActivity(intent)
                                        }
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = MaterialTheme.colorScheme.onBackground)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Play", color = MaterialTheme.colorScheme.onBackground)
                                    }
                                }
                            }
                        }
                    }

                }
            }
}

@Composable
fun RatingBar(voteAverage: Float) {
    val maxRating = 5
    val rating = (voteAverage / 2).coerceIn(0f, maxRating.toFloat())
    val fullStars = rating.toInt()
    val emptyStars = maxRating - fullStars

    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(fullStars) {
            StarIcon("★", tint = Color.Yellow)
        }

        repeat(emptyStars) {
            StarIcon("☆", tint = Color.Gray)
        }
    }
}

@Composable
fun StarIcon(starChar: String, tint: Color) {
    val iconSize = with(LocalDensity.current) { 16.dp }
    Text(
        text = starChar,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        color = tint,
        fontFamily = FontFamily.Default,
        modifier = Modifier.width(iconSize)
    )
}