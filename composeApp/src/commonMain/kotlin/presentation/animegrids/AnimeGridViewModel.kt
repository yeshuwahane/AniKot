package presentation.animegrids

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.repository.AnimeRepositoryImpl
import data.repository.MostWatchedRepositoryImpl
import data.repository.TopAiringRepositoryImpl
import data.utils.DataResource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.home.dashboard.toDashboardUiState

class AnimeGridViewModel(
    private val newAnimeRepository: AnimeRepositoryImpl,
    private val topAiringRepositoryImpl: TopAiringRepositoryImpl,
    private val mostWatchedRepositoryImpl: MostWatchedRepositoryImpl
):ScreenModel {

    private val _uiState = MutableStateFlow(AnimeGridUiState())
    val uiState = _uiState.asStateFlow()


    fun getNewAnime() {
        _uiState.update {
            it.copy(
                animeUiState = DataResource.loading()
            )
        }
        screenModelScope.launch {
            val animes = newAnimeRepository.getNewAnime(1,50)
            val animeMapped = animes.map {
                it?.toDashboardUiState()
            }
            _uiState.update {
                it.copy(
                    animeUiState = DataResource.success(animeMapped)
                )
            }

        }

    }

    fun getAiringAnime() {
        _uiState.update {
            it.copy(animeUiState = DataResource.loading())
        }
        screenModelScope.launch {
            val airingAnimes = topAiringRepositoryImpl.getAiringAnimes(1,50)

            Napier.d("AiringAnimes : ${airingAnimes}")
            _uiState.update {
                it.copy(animeUiState = DataResource.success(airingAnimes.map { it?.toDashboardUiState() }))
            }
        }
    }

    fun getTopAnime(){
        _uiState.update {
            it.copy(animeUiState = DataResource.loading())
        }
        screenModelScope.launch {
            val topAnimes = mostWatchedRepositoryImpl.getMostWatchedAnime(1,50)

            _uiState.update {
                it.copy(animeUiState = DataResource.success(topAnimes.map { it?.toDashboardUiState() }))
            }
        }
    }

    fun getTopRatedAnime(){
        _uiState.update {
            it.copy(animeUiState = DataResource.loading())
        }

        screenModelScope.launch {
            val topRatedAnimes = newAnimeRepository.getTopRatedAnimes(1,50)
            _uiState.update {
                it.copy(animeUiState = DataResource.success(topRatedAnimes.map { it?.toDashboardUiState() }))
            }
        }

    }


}