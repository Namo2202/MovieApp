package com.example.movieappcompose.favorite


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.movieappcompose.data.Movie

@ExperimentalFoundationApi
@Composable
fun FavoritesScreen(navController: NavController,  favorites:MutableState<List<Movie>>) {

        Column(modifier = Modifier.padding(10.dp)) {
            Button(
                modifier = Modifier.padding(bottom = 16.dp),
                onClick = { navController.popBackStack() }
            ) {
                Text(text = "Back", color= MaterialTheme.colorScheme.onBackground)
            }

            LazyColumn(modifier = Modifier.padding(10.dp)) {

                item {
                    if (favorites.value.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 250.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No movies added",
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    } else {
                        favorites.value.forEach { movie ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 5.dp)
                            ) {

                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    val imageSize = "w500"
                                    val fullImageUrl =
                                        "https://image.tmdb.org/t/p/$imageSize/${movie.poster_path}"
                                    Image(
                                        painter = rememberImagePainter(fullImageUrl),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(200.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.FillWidth
                                    )
                                    Text(text = movie.title, textAlign = TextAlign.Center, maxLines = 2)
                                }


                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(
                                    onClick = {  favorites.value = favorites.value.toMutableList().apply {
                                        remove(movie)
                                    } },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
}

