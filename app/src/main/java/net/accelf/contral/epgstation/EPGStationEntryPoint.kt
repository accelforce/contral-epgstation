package net.accelf.contral.epgstation

import androidx.compose.material3.Text
import androidx.navigation.compose.composable
import net.accelf.contral.api.plugin.PluginResolver
import net.accelf.contral.api.plugin.MinorVersion.Companion.minor
import net.accelf.contral.api.plugin.MinorVersion.Companion.patch
import net.accelf.contral.epgstation.pages.AddTimelinePage
import net.accelf.contral.epgstation.timelines.RecordedTimeline

@Suppress("unused")
fun PluginResolver.epgStationPlugin() {
    name = "EPGStation"
    version = 0 minor 0 patch 0

    require("core", 0 minor 8)

    addRoutes {
        composable("epgstation/timelines/add") { AddTimelinePage() }
    }

    addTimeline(RecordedTimeline::class)

    addTimelineAdder(
        render = { Text(text = "Add a EPGStation timeline") },
        onClick = { it("epgstation/timelines/add") },
    )
}
