package presentation.home.dashboard

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

class DashboardViewModel(
    private val newAnimeRepository: AnimeRepositoryImpl,
    private val topAiringRepositoryImpl: TopAiringRepositoryImpl,
    private val mostWatchedRepositoryImpl: MostWatchedRepositoryImpl
):ScreenModel {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    fun getAnime() {
        _uiState.update {
            it.copy(
                dashboardUiState = DataResource.loading()
            )
        }
        screenModelScope.launch {
           val animes = newAnimeRepository.getNewAnime(1,50)

            if (animes.isNotEmpty()){
                val animeMapped = animes.map {
                    it?.toDashboardUiState()
                }
                _uiState.update {
                    it.copy(
                        dashboardUiState = DataResource.success(animeMapped)
                    )
                }
            }else{
                _uiState.update {
                    it.copy(
                        dashboardUiState = DataResource.error(Throwable("Error"))
                    )
                }
            }


        }

    }

    fun getAiringAnimes() {
        _uiState.update {
            it.copy(airingUiState = DataResource.loading())
        }
        screenModelScope.launch {
            val airingAnimes = topAiringRepositoryImpl.getAiringAnimes(1,50)

            if (airingAnimes.isNotEmpty()){
                Napier.d("AiringAnimes : $airingAnimes")
                _uiState.update {
                    it.copy(airingUiState = DataResource.success(airingAnimes.map { it?.toDashboardUiState() }))
                }
            }else{
                _uiState.update {
                    it.copy(
                        airingUiState = DataResource.error(Throwable("Server Error"))
                    )
                }
            }


        }
    }

    fun getTopAnimes(){
        _uiState.update {
            it.copy(topAnimesUiState = DataResource.loading())
        }
        screenModelScope.launch {
            val topAnimes = mostWatchedRepositoryImpl.getMostWatchedAnime(1,6)

            if (topAnimes.isNotEmpty()){
                _uiState.update {
                    it.copy(topAnimesUiState = DataResource.success(topAnimes.map { it?.toDashboardUiState() }))
                }
            }else{
                _uiState.update {
                    it.copy(
                        topAnimesUiState = DataResource.error(Throwable("Server Error"))
                    )
                }
            }
        }
    }

    fun getTopRatedAnimes(){
        _uiState.update {
            it.copy(topRatedAnimesUiState = DataResource.loading())
        }
        screenModelScope.launch {
            val topRatedAnimes = newAnimeRepository.getTopRatedAnimes(1,50)


            if (topRatedAnimes.isNotEmpty()){
                _uiState.update { state ->
                    state.copy(topRatedAnimesUiState = DataResource.success(topRatedAnimes.map { it?.toDashboardUiState() }))
                }
            }else{
                _uiState.update {
                    it.copy(
                        topRatedAnimesUiState = DataResource.error(Throwable("Server Error"))
                    )
                }
            }
        }

    }







}