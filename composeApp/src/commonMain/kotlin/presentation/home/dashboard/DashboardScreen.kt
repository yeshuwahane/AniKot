package presentation.home.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import presentation.AnimeState
import presentation.animegrids.AnimeGridScreen
import presentation.animegrids.AnimeGridShimmer
import presentation.home.animedetails.AnimeDetailScreen

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<DashboardViewModel>()

        val uiState by viewModel.uiState.collectAsState()

        val navigator = LocalNavigator.current

        LaunchedEffect(Unit) {
//            viewModel.getAnime()
            viewModel.getTopAnimes()
            viewModel.getAiringAnimes()
            viewModel.getTopRatedAnimes()
            Napier.d("LaunchedEffect")

        }
        Scaffold(

        ) { paddingValues ->

            Column(modifier = Modifier.padding(paddingValues).background(Color.Black).fillMaxSize().verticalScroll(
                rememberScrollState()
            )) {

                if (uiState.topAnimesUiState.isLoading()){
                    Spacer(modifier = Modifier.height(10.dp))
                    AnimeDashboardTopShimmer()
                }
                else if(uiState.topAnimesUiState.isSuccess()){
                    uiState.topAnimesUiState.data?.let { animeStateList ->
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    navigator?.push(AnimeGridScreen("watched"))
                                }
                        ){
                            Text(
                                text = "Most Watched Anime",
                                color = Color.White,
                                fontSize = 24.sp,
                            )
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight,"", tint = Color.White)
                        }

                        AnimeSlider(animeList = animeStateList, onItemClick = {
                            navigator?.push(AnimeDetailScreen(it))
                        })
                    }
                }else{
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
                        uiState.topAnimesUiState.error?.message?.let { Text(it,color = Color.White) }
                    }
                }

                if (uiState.topRatedAnimesUiState.isLoading()){
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(6){
                            AnimeGridShimmer()
                        }
                    }
                }else if (uiState.topRatedAnimesUiState.isSuccess()){
                    uiState.topRatedAnimesUiState.data?.let { anime ->
                        Row(
                            modifier = Modifier.clickable {
                                navigator?.push(AnimeGridScreen("rated"))
                            }
                        ) {
                            Text(
                                text = "Top Rated Anime",
                                color = Color.White,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight,"", tint = Color.White)
                        }

                        AnimeList(animes = anime, onItemClick = {
                            navigator?.push(AnimeDetailScreen(it))
                        })
                    }
                }else{
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
                        uiState.topRatedAnimesUiState.error?.message?.let { Text(it,color = Color.White) }
                    }
                }



                if (uiState.airingUiState.isLoading()){
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(6){
                            AnimeGridShimmer()
                        }
                    }
                }else if (uiState.airingUiState.isSuccess()){
                    uiState.airingUiState.data?.let { animeStateList ->
                        Row(
                            modifier = Modifier.clickable {
                                navigator?.push(AnimeGridScreen("airing"))
                            }
                        ) {
                            Text(
                                text = "Top Airing Anime",
                                color = Color.White,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight,"", tint = Color.White)
                        }
                        AnimeList(animes = animeStateList, onItemClick = {
                            navigator?.push(AnimeDetailScreen(it))
                        })
                    }
                }else{
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
                        uiState.airingUiState.error?.message?.let { Text(it,color = Color.White) }
                    }
                }



            }

        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun AnimeSlider(animeList: List<AnimeState?>, onItemClick: (id: Int) -> Unit) {

        val pagerState = rememberPagerState(pageCount = {
            animeList.size
        })

        // images slider and slider indicator
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) { page ->
            Box(
                modifier = Modifier.fillMaxWidth().height(250.dp),
                contentAlignment = Alignment.TopStart
            ) {
                KamelImage(
                    asyncPainterResource(animeList.get(page)?.coverImage.toString()),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth().clickable {
                        animeList[page]?.id?.let { onItemClick.invoke(it) }
                    },
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                ) {

                    animeList.get(page)?.englishTitle?.let {
                        Text(
                            it,
                            color = Color.White,
                            fontSize = 24.sp,
                            modifier = Modifier,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    animeList.get(page)?.genre?.let {
                        Text(
                            it.joinToString(","),
                            color = Color.White,
                            modifier = Modifier,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }

            }
        }
        Row(
            Modifier.wrapContentHeight().fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier.padding(2.dp).clip(CircleShape).background(color)
                        .size(10.dp)

                )
            }
        }
    }


    @Composable
    fun AnimeList(animes: List<AnimeState?>, onItemClick: (id: Int) -> Unit) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            items(animes) { anime ->
                if (anime != null) {
                    AnimeItem(anime = anime, onItemClick = {
                        onItemClick.invoke(it)
                    })
                    Napier.d("AnimeItem : ${anime.title}")
                }
            }


        }


    }

    @Composable
    fun AnimeItem(anime: AnimeState, onItemClick: (Int) -> Unit) {
        Column(modifier = Modifier.padding(8.dp).width(150.dp).clickable {
            anime.id?.let { onItemClick.invoke(it) }
        }) {
            anime.coverImage?.let { asyncPainterResource(it) }?.let {
                KamelImage(
                    it,
                    contentDescription = anime.title,
                    modifier = Modifier.height(200.dp).fillMaxWidth()
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
            anime.startYear.let {
                it?.let { it1 ->
                    Text(
                        text = it1,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }


}