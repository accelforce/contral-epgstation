package net.accelf.contral.epgstation.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Version(
    @SerialName("version") val version: String,
)
