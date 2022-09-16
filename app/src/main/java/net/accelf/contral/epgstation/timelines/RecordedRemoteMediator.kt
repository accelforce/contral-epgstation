package net.accelf.contral.epgstation.timelines

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import net.accelf.contral.epgstation.api.EPGStation
import net.accelf.contral.epgstation.api.Recorded
import net.accelf.contral.epgstation.util.Reference
import net.accelf.contral.epgstation.util.nonNegativeOrNull

@OptIn(ExperimentalPagingApi::class)
internal class RecordedRemoteMediator(
    pagingSourceRef: Reference<RecordedPagingSource>,
    private val epgStation: EPGStation,
) : RemoteMediator<Int, Recorded>() {

    private val pagingSource by pagingSourceRef

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Recorded>
    ): MediatorResult =
        when (loadType) {
            LoadType.PREPEND -> pagingSource.firstKeyOrNull()?.let { it - state.config.pageSize }?.nonNegativeOrNull()
            LoadType.APPEND -> pagingSource.lastKeyOrNull()?.let { it + 1 }
            LoadType.REFRESH -> {
                pagingSource.removeAll()
                0
            }
        }
            .runCatching {
                this?.let {
                    epgStation.listRecorded(offset = this, limit = state.config.pageSize).records
                        .mapIndexed { i, item -> this + i to item }
                } ?: emptyList()
            }
            .fold(
                { loaded ->
                    pagingSource.putAll(loaded)
                    MediatorResult.Success(endOfPaginationReached = loaded.isEmpty())
                },
                {
                    MediatorResult.Error(it)
                },
            )
}
