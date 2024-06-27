package domain.repository

import com.yeshuwahane.ani.AnimeByCategoryQuery
import com.yeshuwahane.ani.SearchAnimeQuery

interface SearchAnimeRepository {

    suspend fun searchAnimeByName(search: String, page: Int, perPage: Int): List<SearchAnimeQuery.Medium?>

    suspend fun getGenresList():List<String?>

    suspend fun getAnimeByCategory(category: String, page: Int, perPage: Int): List<AnimeByCategoryQuery.Medium?>

}