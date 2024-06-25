package presentation.animegrids

import data.utils.DataResource
import presentation.AnimeState

data class AnimeGridUiState(
    val animeUiState: DataResource<List<AnimeState?>> = DataResource.initial()
)
