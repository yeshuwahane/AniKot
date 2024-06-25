package domain.repository

import com.yeshuwahane.ani.AiringAnimesQuery
import com.yeshuwahane.ani.AnimeListQuery
import com.yeshuwahane.ani.TopAnimesQuery
import com.yeshuwahane.ani.TopRatedAnimesQuery


interface AnimeRepository {


    suspend fun getNewAnime(page: Int, perPage: Int):List<AnimeListQuery.Medium?>

    suspend fun getTopRatedAnimes(page: Int, perPage: Int): List<TopRatedAnimesQuery.Medium?>



    }