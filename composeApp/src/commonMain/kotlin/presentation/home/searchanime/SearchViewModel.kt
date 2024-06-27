package presentation.home.searchanime

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.repository.SearchAnimeRepositoryImpl
import data.utils.DataResource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    val searchAnimeRepositoryImpl: SearchAnimeRepositoryImpl
):ScreenModel {

   private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    val searchAnimeState = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchAnimeDebounce = searchAnimeState.debounce(1000L)
        .distinctUntilChanged()
        .flatMapLatest { value ->
            if (value.isNotBlank()) {
                searchAnime(value)
            }
            printAnime(value)
        }




    fun searchAnime(name:String){
        _uiState.update {
            it.copy(searchUiState = DataResource.loading())
        }

        screenModelScope.launch {
            val searchAnime = searchAnimeRepositoryImpl.searchAnimeByName(name,1,50)

            if (searchAnime.isNotEmpty()){
                _uiState.update {
                    it.copy(searchUiState = DataResource.success(searchAnime.map { it?.toSearchUiState() }))
                }
            }else{
                _uiState.update { state->
                    state.copy(searchUiState = DataResource.error(Throwable(" No Data")))
                }
            }
        }

        Napier.d("search Anime: $name")
    }

    fun searchByCategory(category:String){
        _uiState.update {
            it.copy(searchUiState = DataResource.loading())
        }
        Napier.d("search Anime category: $category")
        screenModelScope.launch {

            val animeByCategory = searchAnimeRepositoryImpl.getAnimeByCategory(category,1,50)

            if (animeByCategory.isNotEmpty()){
                _uiState.update { state ->
                    state.copy(searchUiState = DataResource.success(animeByCategory.map { it?.toSearchUiState() }))
                }
            }else{
                _uiState.update { state->
                    state.copy(searchUiState = DataResource.error(Throwable("No Data")))
                }
            }

        }

    }


    fun getGenresList(){
        _uiState.update {
            it.copy(
                genreUiState = DataResource.loading()
            )
        }
        screenModelScope.launch {
            val genres = searchAnimeRepositoryImpl.getGenresList()
            _uiState.update {
                it.copy(
                    genreUiState = DataResource.success(genres)
                )
            }
        }
    }



    fun printAnime(query: String): Flow<String> {
        return flowOf(query)
    }

}