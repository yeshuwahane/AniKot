package domain.repository

import com.yeshuwahane.ani.TopAnimesQuery

interface MostWatchedRepository {

    suspend fun getMostWatchedAnime(page: Int, perPage: Int): List<TopAnimesQuery.Medium>

}