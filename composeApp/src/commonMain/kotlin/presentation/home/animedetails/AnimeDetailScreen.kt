package presentation.home.animedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import io.github.aakira.napier.Napier
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import presentation.AnimeState

class AnimeDetailScreen(val id: Int) : Screen {
    @Composable
    override fun Content() {

        val viewModel = getScreenModel<DetailViewModel>()
        val navigator = LocalNavigator.current

        val uiState by viewModel.uiState.collectAsState()
        val scope = rememberCoroutineScope()

        LaunchedEffect(id) {
            scope.launch {
                viewModel.getAnimeDetail(id)
            }
        }

        Scaffold(

        ) { paddingValues ->

            if (uiState.animeUiState.isLoading()) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }else if (uiState.animeUiState.isSuccess()){
                uiState.animeUiState.data?.let { AnimeDetailStatic(anime = it, onBackClick = {navigator?.pop()}) }
            }else{
                uiState.animeUiState.error?.message?.let { Text(it,color = Color.White) }
            }



        }


    }


    @Composable
    fun AnimeDetailStatic(anime: AnimeDetailState,onBackClick: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .verticalScroll(rememberScrollState())
        ) {

            Napier.d("Anime trailer: ${anime.trailer}")


                Box {
                    anime.coverImage?.let { asyncPainterResource(it) }?.let {
                        KamelImage(
                            it,
                            contentDescription = anime.englishTitle,
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    IconButton(
                        onClick = { onBackClick.invoke() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, // Replace with your back icon
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    //trailer ..... in work
                  /*  Button(
                        onClick = { *//* TODO: Handle trailer *//* },
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Text("Watch Trailer")
                    }*/
                }




                Spacer(modifier = Modifier.height(8.dp))
                anime.englishTitle?.let {
                    Text(
                        text = it,
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                anime.genres?.let {
                    Text(
                        text = it.joinToString(", "),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Rating",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = anime.rating.toString(),
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Studio",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        anime.studios?.let {
                            Text(
                                text = it,
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Total Eps",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "${anime.episodes} eps",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Synopsis",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                anime.description?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))


        }

    }


}
