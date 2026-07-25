package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.viewmodel.VpnViewModel

@Composable
fun SettingsScreen(
    viewModel: VpnViewModel,
    modifier: Modifier = Modifier
) {
    val routingMode by viewModel.routingMode.collectAsStateWithLifecycle()
    val logs by viewModel.logs.collectAsStateWithLifecycle()

    var selectedDns by remember { mutableStateOf("Cloudflare (1.1.1.1)") }
    var enableMux by remember { mutableStateOf(false) }
    var enableSniffing by remember { mutableStateOf(true) }
    var showLogModal by remember { mutableStateOf(false) }
    var showDnsDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Settings & Tuning",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = CyberTextPrimary
        )
        Text(
            text = "Configure core V2ray routing, DNS, and logging parameters",
            style = MaterialTheme.typography.bodySmall,
            color = CyberTextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Routing Section
        SettingsGroupHeader(title = "ROUTING & RULES")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(CyberSurface)
                .border(1.dp, CyberCardBorder, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column {
                SettingsRow(
                    icon = Icons.Default.AltRoute,
                    title = "Routing Mode",
                    subtitle = "Current: $routingMode",
                    onClick = {
                        val next = when (routingMode) {
                            "Smart Proxy" -> "Global VPN"
                            "Global VPN" -> "Bypass LAN"
                            else -> "Smart Proxy"
                        }
                        viewModel.setRoutingMode(next)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsToggleRow(
                    icon = Icons.Default.Tune,
                    title = "Domain Sniffing (HTTP/TLS)",
                    subtitle = "Sniff target domains for smart routing",
                    checked = enableSniffing,
                    onCheckedChange = { enableSniffing = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // DNS & Proxy Engine Section
        SettingsGroupHeader(title = "DNS & NETWORK ENGINE")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(CyberSurface)
                .border(1.dp, CyberCardBorder, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column {
                SettingsRow(
                    icon = Icons.Default.Dns,
                    title = "DNS Server Provider",
                    subtitle = selectedDns,
                    onClick = { showDnsDialog = true }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsToggleRow(
                    icon = Icons.Default.Tune,
                    title = "Multiplexing (Mux)",
                    subtitle = "Reuse TCP connections to reduce latency",
                    checked = enableMux,
                    onCheckedChange = { enableMux = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // V2ray Core Console Logs
        SettingsGroupHeader(title = "DIAGNOSTICS & LOGS")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(CyberSurface)
                .border(1.dp, CyberCardBorder, RoundedCornerShape(18.dp))
                .clickable { showLogModal = true }
                .padding(16.dp)
                .testTag("open_logs_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = "Logs",
                    tint = CyberCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("V2ray Console Logs", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = CyberTextPrimary)
                    Text("View real-time engine stdout & debug messages", style = MaterialTheme.typography.labelSmall, color = CyberTextMuted)
                }
                Icon(Icons.Default.ChevronRight, contentDescription = "View", tint = CyberCyan)
            }
        }

        // DNS Dialog
        if (showDnsDialog) {
            AlertDialog(
                onDismissRequest = { showDnsDialog = false },
                containerColor = CyberSurface,
                title = { Text("Select Secure DNS Server", fontWeight = FontWeight.Bold, color = CyberTextPrimary) },
                text = {
                    Column {
                        listOf("Cloudflare (1.1.1.1)", "Google (8.8.8.8)", "AdGuard Anti-Ad (94.140.14.14)", "Quad9 (9.9.9.9)").forEach { dns ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedDns = dns
                                        showDnsDialog = false
                                        viewModel.showToast("DNS changed to $dns")
                                    }
                                    .padding(vertical = 8.dp)
                            ) {
                                RadioButton(
                                    selected = selectedDns == dns,
                                    onClick = {
                                        selectedDns = dns
                                        showDnsDialog = false
                                        viewModel.showToast("DNS changed to $dns")
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = CyberCyan, unselectedColor = CyberTextMuted)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(dns, color = CyberTextPrimary, fontSize = 13.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDnsDialog = false }) {
                        Text("Close", color = CyberCyan)
                    }
                }
            )
        }

        // V2ray Core Console Log Viewer Modal
        if (showLogModal) {
            AlertDialog(
                onDismissRequest = { showLogModal = false },
                containerColor = CyberSurface,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("V2ray Core Logs", fontWeight = FontWeight.Bold, color = CyberTextPrimary)
                        IconButton(onClick = { viewModel.engineManager.clearLogs() }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear", tint = CyberCyan)
                        }
                    }
                },
                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF070A0F))
                            .padding(12.dp)
                    ) {
                        val logState = rememberScrollState()
                        Column(
                            modifier = Modifier.verticalScroll(logState)
                        ) {
                            if (logs.isEmpty()) {
                                Text(
                                    text = "[INFO] V2ray log stream ready.",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = CyberTextMuted
                                )
                            } else {
                                logs.forEach { line ->
                                    Text(
                                        text = line,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = if (line.contains("SUCCESS") || line.contains("Active")) CyberEmerald else CyberCyan,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showLogModal = false }) {
                        Text("Close Log", color = CyberCyan)
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingsGroupHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = CyberTextMuted,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = CyberCyan, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = CyberTextPrimary)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = CyberTextMuted)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = CyberCyan)
    }
}

@Composable
private fun SettingsToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = CyberCyan, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = CyberTextPrimary)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = CyberTextMuted)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = CyberCyan,
                uncheckedThumbColor = CyberTextMuted,
                uncheckedTrackColor = CyberCardBorder
            )
        )
    }
}
