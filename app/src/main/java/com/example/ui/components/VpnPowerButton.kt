package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VpnConnectionState
import com.example.ui.theme.CyberAmber
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.CyberRose
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary

@Composable
fun VpnPowerButton(
    connectionState: VpnConnectionState,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = connectionState is VpnConnectionState.Connected
    val isConnecting = connectionState is VpnConnectionState.Connecting || connectionState is VpnConnectionState.Disconnecting

    // Pulsing animation for connecting / active state
    val infiniteTransition = rememberInfiniteTransition(label = "power_glow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isConnecting || isConnected) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val buttonColor by animateColorAsState(
        targetValue = when {
            isConnected -> CyberEmerald
            isConnecting -> CyberAmber
            else -> Color(0xFF1E293B)
        },
        animationSpec = tween(400),
        label = "btn_color"
    )

    val glowColor = when {
        isConnected -> CyberEmerald.copy(alpha = 0.25f)
        isConnecting -> CyberAmber.copy(alpha = 0.25f)
        else -> CyberCyan.copy(alpha = 0.08f)
    }

    val stateText = when (connectionState) {
        is VpnConnectionState.Connected -> "CONNECTED"
        is VpnConnectionState.Connecting -> "CONNECTING..."
        is VpnConnectionState.Disconnecting -> "DISCONNECTING..."
        is VpnConnectionState.Disconnected -> "TAP TO CONNECT"
    }

    val statusSubtitle = when (connectionState) {
        is VpnConnectionState.Connected -> "V2ray Tunnel Secured"
        is VpnConnectionState.Connecting -> connectionState.progressMessage
        is VpnConnectionState.Disconnecting -> connectionState.message
        is VpnConnectionState.Disconnected -> "Protected by V2ray Protocol"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // Outer Glowing Ring 2
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(if (isConnected || isConnecting) pulseScale else 1f)
                    .clip(CircleShape)
                    .background(glowColor)
            )

            // Outer Glowing Ring 1
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(buttonColor.copy(alpha = 0.15f))
                    .border(
                        width = 2.dp,
                        brush = Brush.radialGradient(
                            colors = listOf(buttonColor, buttonColor.copy(alpha = 0.2f))
                        ),
                        shape = CircleShape
                    )
            )

            // Main Interactive Circle Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .testTag("vpn_power_button")
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                buttonColor,
                                buttonColor.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggle
                    )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (isConnected) Icons.Default.Shield else Icons.Default.PowerSettingsNew,
                        contentDescription = "Power Toggle",
                        tint = if (isConnected || isConnecting) Color.Black else CyberCyan,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stateText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = when {
                isConnected -> CyberEmerald
                isConnecting -> CyberAmber
                else -> CyberTextPrimary
            },
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = statusSubtitle,
            style = MaterialTheme.typography.bodySmall,
            color = CyberTextSecondary
        )
    }
}
