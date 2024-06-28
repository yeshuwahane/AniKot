package domain.repository

import androidx.paging.PagingSource
import com.yeshuwahane.ani.AiringAnimesQuery

interface AiringAnimePagingRepository {
    suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, AiringAnimesQuery.Medium>
}