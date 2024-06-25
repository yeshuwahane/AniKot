package domain.repository

import com.yeshuwahane.ani.AiringAnimesQuery

interface TopAiringRepository {

    suspend fun getAiringAnimes(page: Int, perPage: Int): List<AiringAnimesQuery.Medium?>

}