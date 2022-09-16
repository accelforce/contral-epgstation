package net.accelf.contral.epgstation.pages

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import net.accelf.contral.api.timelines.Timeline
import net.accelf.contral.api.ui.LocalNavController
import net.accelf.contral.api.ui.LocalRegisterTimeline
import net.accelf.contral.api.ui.theme.ContralTheme
import net.accelf.contral.api.ui.utils.useState
import net.accelf.contral.epgstation.api.EPGStation
import net.accelf.contral.epgstation.timelines.RecordedTimeline

@Composable
internal fun AddTimelinePage() {
    val navController = LocalNavController.current
    val registerTimeline = LocalRegisterTimeline.current

    AddTimeline(
        navController = navController,
        registerTimeline = registerTimeline,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddTimeline(
    navController: NavController,
    registerTimeline: suspend (Timeline) -> Long,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var secure by useState(false)
    var domain by useState("")
    var loading by useState(false)
    var error by useState("")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
    ) {
        Text(
            text = "Enter domain",
            style = MaterialTheme.typography.titleLarge,
        )

        OutlinedTextField(
            value = domain,
            onValueChange = {
                domain = it
            },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            enabled = !loading,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = secure,
                onCheckedChange = {
                    secure = it
                },
                enabled = !loading,
            )

            Text(
                text = "Use HTTPS for the connection",
            )
        }

        Button(
            onClick = {
                scope.launch {
                    loading = true
                    error = ""
                    runCatching {
                        val epgStation = EPGStation.create(secure, domain)
                        epgStation.getVersion()
                    }
                        .fold(
                            {
                                Toast.makeText(context, "EPGStation Version ${it.version}", Toast.LENGTH_SHORT)
                                    .show()
                                val id = registerTimeline(RecordedTimeline(secure, domain))
                                navController.popBackStack()
                                navController.navigate("timelines/$id")
                            },
                            {
                                error = it.localizedMessage ?: it.message ?: "Failed to fetch version"
                            },
                        )
                    loading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
        ) {
            Text(text = "Add as timeline")
        }

        if (error.isNotBlank()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
@Preview
private fun PreviewAddTimeline() {
    ContralTheme {
        AddTimeline(
            navController = rememberNavController(),
            registerTimeline = { 0 },
        )
    }
}
