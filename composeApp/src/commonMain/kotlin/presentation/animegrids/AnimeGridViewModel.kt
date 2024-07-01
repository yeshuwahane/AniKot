package presentation.animegrids

import androidx.paging.cachedIn
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.yeshuwahane.ani.AiringAnimesQuery
import data.repository.AnimeRepositoryImpl
import data.repository.MostWatchedRepositoryImpl
import data.repository.TopAiringRepositoryImpl
import data.repository.paging.AiringAnimesPagingSource
import data.repository.paging.MostWatchedAnimePagingSource
import data.utils.DataResource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import presentation.AnimeState
import presentation.home.dashboard.toDashboardUiState

class AnimeGridViewModel(
    private val newAnimeRepository: AnimeRepositoryImpl,
    private val topAiringRepositoryImpl: TopAiringRepositoryImpl,
    private val mostWatchedRepositoryImpl: MostWatchedRepositoryImpl
) : ScreenModel {

    private val _uiState = MutableStateFlow(AnimeGridUiState())
    val uiState = _uiState.asStateFlow()


    fun getNewAnime() {
        _uiState.update {
            it.copy(
                animeUiState = DataResource.loading()
            )
        }
        screenModelScope.launch {
            val animes = newAnimeRepository.getNewAnime(1,100)
            if (animes.isNotEmpty()){
                val animeMapped = animes.map {
                    it?.toDashboardUiState()
                }
                _uiState.update {
                    it.copy(
                        animeUiState = DataResource.success(animeMapped),
                        categoryName = "New Anime Series"
                    )
                }
            }else{
                _uiState.update {
                    it.copy(
                        animeUiState = DataResource.error(Throwable("Server Error"))
                    )
                }
            }


        }

    }

    fun getAiringAnime() {
        _uiState.update {
            it.copy(animeUiState = DataResource.loading())
        }
        screenModelScope.launch {
            val airingAnimes = topAiringRepositoryImpl.getAiringAnimes(1,200)

            if (airingAnimes.isNotEmpty()){
                _uiState.update {
                    it.copy(
                        animeUiState = DataResource.success(airingAnimes.map { it.toDashboardUiState() }),
                        categoryName = "Current Airing Anime"
                    )
                }
            }else{
                _uiState.update {
                    it.copy(
                        animeUiState = DataResource.error(Throwable("Server Error"))
                    )
                }
            }


        }
    }

    fun getMostWatchedAnime() {
        _uiState.update {
            it.copy(animeUiState = DataResource.loading())
        }
        screenModelScope.launch {
            val topAnimes = mostWatchedRepositoryImpl.getMostWatchedAnime(1,200)

            if (topAnimes.isNotEmpty()){
                _uiState.update {
                    it.copy(
                        animeUiState = DataResource.success(topAnimes.map { it?.toDashboardUiState() }),
                        categoryName = "Most Watched Anime Series"
                    )
                }
            }else{
                _uiState.update {
                    it.copy(
                        animeUiState = DataResource.error(Throwable("Server Error"))
                    )
                }
            }


        }
    }

    fun getTopRatedAnime() {
        _uiState.update {
            it.copy(animeUiState = DataResource.loading())
        }
        screenModelScope.launch {
            val topRatedAnimes = newAnimeRepository.getTopRatedAnimes(1,100)
            if (topRatedAnimes.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        animeUiState = DataResource.success(topRatedAnimes.map { it?.toDashboardUiState() }),
                        categoryName = "Top Rated Anime Series"
                    )

                }
            }else{
                _uiState.update {
                    it.copy(
                        animeUiState = DataResource.error(Throwable("Server Error"))
                    )
                }
            }

        }

    }


}