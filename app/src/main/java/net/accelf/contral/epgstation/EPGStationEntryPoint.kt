package net.accelf.contral.epgstation

import net.accelf.contral.api.plugin.PluginResolver
import net.accelf.contral.api.plugin.MinorVersion.Companion.minor
import net.accelf.contral.api.plugin.MinorVersion.Companion.patch

@Suppress("unused")
fun PluginResolver.epgStationPlugin() {
    name = "EPGStation"
    version = 0 minor 0 patch 0

    require("core", 0 minor 8)
}
