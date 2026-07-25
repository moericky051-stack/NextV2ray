package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.SpeedTestDial
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.CyberRose
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.viewmodel.VpnViewModel

@Composable
fun StatsScreen(
    viewModel: VpnViewModel,
    modifier: Modifier = Modifier
) {
    val isTesting by viewModel.speedTestRunning.collectAsStateWithLifecycle()
    val progress by viewModel.speedTestProgress.collectAsStateWithLifecycle()
    val pingMs by viewModel.speedTestPingMs.collectAsStateWithLifecycle()
    val downloadMbps by viewModel.speedTestDownloadMbps.collectAsStateWithLifecycle()
    val uploadMbps by viewModel.speedTestUploadMbps.collectAsStateWithLifecycle()
    val selectedServer by viewModel.selectedServer.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Speed & Network Stats",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = CyberTextPrimary
                )
                Text(
                    text = "Measure real-time bandwidth latency & bandwidth throughput",
                    style = MaterialTheme.typography.bodySmall,
                    color = CyberTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Speed Test Gauge Container
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CyberSurface)
                .border(1.dp, CyberCardBorder, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SpeedTestDial(
                    downloadMbps = downloadMbps,
                    uploadMbps = uploadMbps,
                    isTesting = isTesting,
                    progress = progress
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Row (Ping / Download / Upload)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Ping
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("PING", style = MaterialTheme.typography.labelSmall, color = CyberTextMuted, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (pingMs > 0) "$pingMs ms" else "--",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CyberEmerald
                        )
                    }

                    // Download
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("DOWNLOAD", style = MaterialTheme.typography.labelSmall, color = CyberTextMuted, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = String.format("%.1f Mbps", downloadMbps),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CyberCyan
                        )
                    }

                    // Upload
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("UPLOAD", style = MaterialTheme.typography.labelSmall, color = CyberTextMuted, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = String.format("%.1f Mbps", uploadMbps),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CyberTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { viewModel.runSpeedTest() },
                    enabled = !isTesting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberCyan,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .testTag("run_speed_test_btn")
                ) {
                    if (isTesting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.Black,
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Testing Bandwidth...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Start Test")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("RUN SPEED TEST", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Data Usage Overview Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CyberSurface)
                .border(1.dp, CyberCardBorder, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column {
                Text(
                    text = "TRAFFIC DATA USAGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = CyberTextMuted,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    UsageColumn(title = "TODAY", usage = "1.42 GB", color = CyberCyan)
                    UsageColumn(title = "THIS WEEK", usage = "12.8 GB", color = CyberEmerald)
                    UsageColumn(title = "THIS MONTH", usage = "48.2 GB", color = CyberTextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Network Diagnostics Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CyberSurface)
                .border(1.dp, CyberCardBorder, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column {
                Text(
                    text = "NETWORK DIAGNOSTICS",
                    style = MaterialTheme.typography.labelSmall,
                    color = CyberTextMuted,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                DiagRow(label = "Virtual IP", value = selectedServer?.ipAddress ?: "104.21.48.12")
                DiagRow(label = "ISP Carrier", value = "Cloudflare / Anycast Net")
                DiagRow(label = "Protocol Mode", value = selectedServer?.protocol?.displayName ?: "VLESS TLS")
                DiagRow(label = "DNS Server", value = "1.1.1.1 (Cloudflare Secure)")
            }
        }
    }
}

@Composable
private fun UsageColumn(title: String, usage: String, color: Color) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(title, style = MaterialTheme.typography.labelSmall, color = CyberTextMuted, fontSize = 10.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(usage, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun DiagRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = CyberTextSecondary)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = CyberTextPrimary)
    }
}
