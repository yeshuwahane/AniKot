package data.repository.paging

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.yeshuwahane.ani.AiringAnimesQuery
import com.yeshuwahane.ani.TopAnimesQuery
import data.repository.MostWatchedRepositoryImpl

class MostWatchedAnimePagingSource(
    private val repository: MostWatchedRepositoryImpl
) : PagingSource<Int, TopAnimesQuery.Medium>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopAnimesQuery.Medium> {
        return try {
            val currentPage = params.key ?: 1
            val animes = repository.getMostWatchedAnime(currentPage,params.loadSize)
            LoadResult.Page(
                data = animes,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (animes.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: androidx.paging.PagingState<Int, TopAnimesQuery.Medium>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


}