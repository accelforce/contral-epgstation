package net.accelf.contral.epgstation.timelines

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.accelf.contral.epgstation.api.Recorded
import net.accelf.contral.epgstation.util.firstKeyOrNull
import net.accelf.contral.epgstation.util.lastKeyOrNull
import net.accelf.contral.epgstation.util.nonNegativeOrNull
import java.util.*

internal class RecordedPagingSource(
    private val records: TreeMap<Int, Recorded>,
) : PagingSource<Int, Recorded>() {

    override fun getRefreshKey(state: PagingState<Int, Recorded>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recorded> =
        when (params) {
            is LoadParams.Prepend -> records.headMap(params.key, false)
            is LoadParams.Append -> records.tailMap(params.key, false)
            is LoadParams.Refresh -> records
        }
            .let { loaded ->
                LoadResult.Page(
                    loaded.map { (_, it) -> it },
                    ((loaded.firstKeyOrNull() ?: -1) - params.loadSize).nonNegativeOrNull(),
                    loaded.lastKeyOrNull()?.let { it + 1 },
                )
            }

    fun firstKeyOrNull() = records.firstKeyOrNull()

    fun lastKeyOrNull() = records.lastKeyOrNull()

    fun removeAll() {
        records.clear()
        invalidate()
    }

    fun putAll(items: List<Pair<Int, Recorded>>) {
        records.putAll(items)
        invalidate()
    }
}
