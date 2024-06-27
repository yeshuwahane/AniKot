package presentation.home.animedetails

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.repository.AnimeRepositoryImpl
import data.utils.DataResource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(val animeRepositoryImpl: AnimeRepositoryImpl):ScreenModel {

    private val _uiState = MutableStateFlow(AnimeDetailUiState())
    val uiState = _uiState.asStateFlow()


    fun getAnimeDetail(mediaId: Int) {
        _uiState.update {
            it.copy(animeUiState = DataResource.loading())
        }
        screenModelScope.launch {
            val animeDetail = animeRepositoryImpl.getAnimeDetails(mediaId)

            Napier.d("Anime trailer: ${animeDetail.trailer}")
            if (animeDetail.title?.native?.isNotBlank() == true){
                _uiState.update {
                    it.copy(animeUiState = DataResource.success(animeDetail.toAnimeDetailState()))
                }
            }else{
                _uiState.update {
                    it.copy(animeUiState = DataResource.error(Throwable("Server Error")))
                }
            }


        }

    }


}