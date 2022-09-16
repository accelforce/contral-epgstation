package net.accelf.contral.epgstation.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class VideoFile(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)
