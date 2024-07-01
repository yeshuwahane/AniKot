package data.repository.paging

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import com.yeshuwahane.ani.AiringAnimesQuery
import data.repository.TopAiringRepositoryImpl


class AiringAnimesPagingSource(
    private val repository: TopAiringRepositoryImpl
) : PagingSource<Int, AiringAnimesQuery.Medium>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AiringAnimesQuery.Medium> {
        return try {
            val currentPage = params.key ?: 1
            val animes = repository.getAiringAnimes(currentPage, params.loadSize)
            LoadResult.Page(
                data = animes,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (animes.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AiringAnimesQuery.Medium>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

