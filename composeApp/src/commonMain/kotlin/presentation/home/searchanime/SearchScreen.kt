package presentation.home.searchanime

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.Rgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.plusmobileapps.konnectivity.Konnectivity
import com.plusmobileapps.konnectivity.NetworkConnection
import io.github.aakira.napier.Napier
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import presentation.AnimeState
import presentation.animegrids.AnimeGridShimmer
import presentation.home.animedetails.AnimeDetailScreen

class SearchScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = getScreenModel<SearchViewModel>()

        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow


        LaunchedEffect(Unit) {
            viewModel.getGenresList()
        }


        val searchAnimeValue by viewModel.searchAnimeState.collectAsState()
        val searchAnimeText by viewModel.searchAnimeDebounce.collectAsState(initial = "")


        val konnectivity = Konnectivity()
        val networkConnection: NetworkConnection = konnectivity.currentNetworkConnection

        var isNetworkConnected by remember { mutableStateOf(true) }


        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }


        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->

            when (networkConnection) {
                NetworkConnection.NONE -> {
                    isNetworkConnected = false
                    scope.launch {
                        snackbarHostState.showSnackbar("No Network Connection")
                    }
                }
                NetworkConnection.WIFI -> {
                    if (!isNetworkConnected){
                        isNetworkConnected = true
                        scope.launch {
                            snackbarHostState.showSnackbar("Connection Back")
                        }
                    }
                }
                NetworkConnection.CELLULAR -> {
                    if (!isNetworkConnected){
                        isNetworkConnected = true
                        scope.launch {
                            snackbarHostState.showSnackbar("Connection Back")
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(paddingValues)
            ){

                SearchBar(
                    textValue = searchAnimeValue,
                    onValueChange = {
                        viewModel.searchAnimeState.value = it
                    }
                )

                if (searchAnimeValue.isEmpty()) {
                    uiState.genreUiState.data?.let { genres ->
                        CategorySection(
                            genres = genres,
                            onCategoryClick = {
                                viewModel.searchByCategory(it)
                            }
                        )
                    }
                }


                if (uiState.searchUiState.isLoading()) {
                    LazyColumn(
                        modifier = Modifier.background(Color.Black).fillMaxWidth()
                    ) {

                        items(6) {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                items(2) {
                                    AnimeSearchShimmer()
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                        }

                    }

                } else if (uiState.searchUiState.isSuccess()) {

                    uiState.searchUiState.data?.let {
                        AnimeList(
                            animes = it,
                            onItemClick = {
                                navigator.push(AnimeDetailScreen(it))
                            }
                        )
                    }
                } else{
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
                        uiState.searchUiState.error?.message?.let { Text(it,color = Color.White) }
                    }
                }

            }
        }


    }


    @Composable
    fun SearchBar(textValue: String, onValueChange: (String) -> Unit) {
        TextField(
            value = textValue,
            onValueChange = {
                onValueChange.invoke(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = {
                Text(text = "Search", color = Color.Gray)
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }

    @Composable
    fun CategorySection(
        genres: List<String?>,
        onCategoryClick: (String) -> Unit
    ) {
        var selectedCategory by remember { mutableStateOf<String?>(null) }

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(genres) { genre ->
                if (genre != null) {
                    CategoryButton(
                        category = genre,
                        isSelected = genre == selectedCategory,
                        onCategoryClick = {
                            selectedCategory = if (selectedCategory == it) null else it
                            onCategoryClick.invoke(selectedCategory ?: "")
                        })
                }
            }
        }
    }

    @Composable
    fun CategoryButton(category: String, isSelected: Boolean, onCategoryClick: (String) -> Unit) {
        Button(
            onClick = { onCategoryClick.invoke(category) },
            colors = ButtonDefaults.buttonColors(backgroundColor = if (isSelected) Color(0xffff9532) else Color.Gray),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(4.dp)
        ) {
            Text(text = category, color = Color.White, textAlign = TextAlign.Center)
        }
    }

    @Composable
    fun AnimeList(animes: List<AnimeState?>, onItemClick: (Int) -> Unit) {

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(animes.chunked(2)) { rowAnimes ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowAnimes.forEach { anime ->
                        if (anime != null) {
                            AnimeItem(anime = anime, onItemClick = { onItemClick.invoke(it) })
                            Napier.d("AnimeItem : ${anime.title}")
                        }
                    }
                }
            }
        }


    }

    @Composable
    fun AnimeItem(anime: AnimeState, onItemClick: (Int) -> Unit) {
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


}