package net.accelf.contral.epgstation.timelines

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.accelf.contral.api.composers.Composer
import net.accelf.contral.api.timelines.Timeline
import net.accelf.contral.api.timelines.TimelineItem
import net.accelf.contral.epgstation.api.EPGStation
import net.accelf.contral.epgstation.api.Recorded
import net.accelf.contral.epgstation.util.Reference
import java.util.*

@Serializable
@SerialName("epgstation/recorded")
internal class RecordedTimeline(
    @SerialName("secure") val secure: Boolean,
    @SerialName("domain") val domain: String,
) : Timeline {

    @Transient
    private val records = TreeMap<Int, Recorded>()

    @Transient
    val baseUrl = EPGStation.baseUrl(secure, domain)
    @Transient
    private val epgStation = EPGStation.create(baseUrl)

    @Transient
    private val pagingSourceRef = Reference<RecordedPagingSource>()
    private var pagingSource by pagingSourceRef

    @Composable
    @SuppressLint("ComposableNaming")
    @OptIn(ExperimentalPagingApi::class)
    override fun getPager(setPager: (Pager<*, out TimelineItem>) -> Unit) {
        setPager(
            Pager(
                config = PagingConfig(20),
                remoteMediator = RecordedRemoteMediator(pagingSourceRef, epgStation),
            ) {
                RecordedPagingSource(records).also { pagingSource = it }
            },
        )
    }

    @Composable
    @SuppressLint("ComposableNaming")
    override fun getComposer(setComposer: (Composer) -> Unit) {
        setComposer(object : Composer {
            override suspend fun compose(content: String): Boolean = false
        })
    }

    @Composable
    override fun Render() {
        RenderTimeline(domain = domain)
    }
}

@Composable
private fun RenderTimeline(
    domain: String,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
    ) {
        Icon(
            imageVector = Icons.Default.Tv,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(2.dp),
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "EPGStation",
                maxLines = 1,
            )

            Text(
                text = domain,
                maxLines = 1,
            )
        }
    }
}

@Composable
@Preview
private fun PreviewRenderTimeline() {
    RenderTimeline(domain = "example.com")
}
