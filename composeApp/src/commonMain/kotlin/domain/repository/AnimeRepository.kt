package domain.repository

import data.dto.AnimeDetailsDto
import data.dto.AnimeListDto
import data.utils.DataResource

interface AnimeRepository {

    suspend fun getDashboardAnime():DataResource<List<AnimeListDto>>

    suspend fun getAnimeByID(id:Int):DataResource<AnimeDetailsDto>


}