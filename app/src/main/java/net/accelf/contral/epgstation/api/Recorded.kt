package net.accelf.contral.epgstation.api

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.accelf.contral.api.timelines.LocalTimeline
import net.accelf.contral.api.timelines.TimelineItem
import net.accelf.contral.api.ui.theme.ContralTheme
import net.accelf.contral.epgstation.timelines.RecordedTimeline

@Serializable
internal data class Recorded(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("thumbnails") val thumbnailIds: List<Int>,
    @SerialName("videoFiles") val videoFiles: List<VideoFile>,
) : TimelineItem {

    @Composable
    override fun Render() {
        val timeline = LocalTimeline.current as RecordedTimeline

        Render(
            thumbnailUrl = thumbnailIds.firstOrNull()?.toString()?.let {
                timeline.baseUrl.newBuilder()
                    .addPathSegment("api")
                    .addPathSegment("thumbnails")
                    .addPathSegment(it)
                    .build()
                    .toString()
            },
        )
    }

    @Composable
    fun Render(
        thumbnailUrl: String?,
    ) {
        Column {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "Thumbnail",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(18f / 9)
                    .padding(2.dp),
            )

            Text(
                text = name,
                modifier = Modifier.padding(horizontal = 4.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

@Composable
@Preview
private fun PreviewRenderRecorded() {
    val recorded = Recorded(
        id = 123,
        name = "Program Name",
        thumbnailIds = listOf(1),
        videoFiles = emptyList(),
    )

    ContralTheme {
        recorded.Render(
            thumbnailUrl = "",
        )
    }
}
