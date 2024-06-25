package presentation.home.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import io.github.aakira.napier.Napier
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import presentation.AnimeState

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<DashboardViewModel>()

        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
//            viewModel.getAnime()
//            viewModel.getTopAnimes()
//            viewModel.getAiringAnimes()
            viewModel.getTopRatedAnimes()
            Napier.d("LaunchedEffect")

        }
        Scaffold { paddingValues ->

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Series", fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))

                uiState.topAnimesUiState.data?.let { AnimeList(animes = it) }
            }

        }
    }

    @Composable
    fun AnimeList(animes: List<AnimeState?>) {

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
                            AnimeItem(anime = anime)
                            Napier.d("AnimeItem : ${anime.title}")
                        }
                    }
                }
            }
        }


    }

    @Composable
    fun AnimeItem(anime: AnimeState) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(150.dp)
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
                anime.title?.let { Text(text = it, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
            } else {
                anime.englishTitle.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            anime.startYear.let { it?.let { it1 -> Text(text = it1, fontSize = 12.sp) } }
        }
    }
}