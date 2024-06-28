package presentation.animegrids

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.utils.DataResource
import io.github.aakira.napier.Napier
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import presentation.AnimeState
import presentation.home.animedetails.AnimeDetailScreen

class AnimeGridScreen(val condition: String) : Screen {
    @Composable
    override fun Content() {

        val viewModel = getScreenModel<AnimeGridViewModel>()
        val scope = rememberCoroutineScope()

        val uiState by viewModel.uiState.collectAsState()

        var currentPage by remember { mutableStateOf(1) }
        val totalPages = 7
        val pageSize = 50

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(currentPage) {
            when (condition) {
                "watched" -> {
                    scope.launch {
                        viewModel.getMostWatchedAnime(currentPage,pageSize)
                    }
                }

                "rated" -> {
                    scope.launch {
                        viewModel.getTopRatedAnime(currentPage,pageSize)
                    }
                }

                "airing" -> {
                    scope.launch {
                        viewModel.getAiringAnime(currentPage,50)
                    }
                }

                else -> {
                    scope.launch {
                        viewModel.getNewAnime(currentPage,pageSize)
                    }
                }
            }



            Napier.d("LaunchedEffect")

        }
        Scaffold { paddingValues ->

            if (uiState.animeUiState.isLoading()){
                LazyColumn(
                    modifier = Modifier.background(Color.Black).fillMaxWidth().padding(paddingValues)
                ) {

                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = { navigator.pop() },
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

                            uiState.categoryName?.let { category -> Text(text = category, fontSize = 24.sp, color = Color.White, modifier = Modifier.align(Alignment.Center)) }
                        }
                    }
                    items(6){
                        LazyRow(
                            modifier = Modifier.fillMaxWidth().padding(paddingValues),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            items(2){
                                AnimeGridShimmer()
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }

            }else if (uiState.animeUiState.isSuccess()){
                uiState.animeUiState.data?.let {
                    Column(modifier = Modifier.padding(paddingValues).background(Color.Black)) {

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = { navigator.pop() },
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

                            uiState.categoryName?.let { category -> Text(text = category, fontSize = 24.sp, color = Color.White, modifier = Modifier.align(Alignment.Center)) }
                        }

                        AnimeList(
                            animes = it,
                            onItemClick = {
                                navigator.push(AnimeDetailScreen(it))
                            },
                            loadMore = {
                                viewModel.loadMoreAnimes()
                            }
                        )

                    }
                }

            }else{
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
                    uiState.animeUiState.error?.message?.let { Text(it,color = Color.White) }
                }
            }



        }
    }

    @Composable
    fun Header(paddingValues: PaddingValues,category:String,onBackClick:()->Unit) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { /* Handle back navigation */ },
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

            // Display category name
            Text(
                text = "Current Airing Anime",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }


    @Composable
    fun AnimeList(animes: List<AnimeState?>, loadMore: () -> Unit,onItemClick: (Int) -> Unit) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(animes.chunked(2)) { rowAnimes ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowAnimes.forEach { anime ->
                        if (anime != null) {
                            AnimeItem(anime = anime, onItemClick = { /* Handle item click */ })
                        }
                    }
                }
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .map { it.lastOrNull() }
                .distinctUntilChanged()
                .collect { lastVisibleItem ->
                    if (lastVisibleItem != null && lastVisibleItem.index >= animes.size - 1) {
                        loadMore()
                    }
                }
        }
    }


    @Composable
    fun AnimeItem(anime: AnimeState,onItemClick: (Int) -> Unit) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(150.dp)
                .clickable {
                    anime.id?.let { onItemClick.invoke(it) }
                }

        ) {
            anime.coverImage?.let { asyncPainterResource(it) }?.let {
                KamelImage(
                    it,
                    contentDescription = anime.title,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            if (anime.englishTitle.isNullOrEmpty()) {
                anime.title?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                anime.englishTitle.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            anime.startYear.let { it?.let { it1 -> Text(text = it1, fontSize = 12.sp) } }
        }
    }


    @Composable
    fun LoadingContent(paddingValues: PaddingValues,category: String,onBackClick: () -> Unit) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                Header(paddingValues,category, onBackClick = {onBackClick.invoke()} )
            }
            items(6) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    items(2) {
                        AnimeGridShimmer()
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }


    @Composable
    fun ErrorContent(error: Throwable, paddingValues: PaddingValues) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = error.message ?: "An error occurred",
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }

    @Composable
    fun NoDataContent(paddingValues: PaddingValues) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No data available",
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }


}