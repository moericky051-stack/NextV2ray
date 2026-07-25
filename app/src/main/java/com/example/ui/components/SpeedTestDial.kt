package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.CyberRose
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedTestDial(
    downloadMbps: Float,
    uploadMbps: Float,
    isTesting: Boolean,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val currentMbps = if (progress <= 0.6f) downloadMbps else uploadMbps
    val animatedMbps by animateFloatAsState(
        targetValue = currentMbps,
        animationSpec = tween(150),
        label = "mbps_animated"
    )

    // Gauge angle maps 0..150 Mbps -> 135deg to 405deg
    val maxMbps = 150f
    val sweepFraction = (animatedMbps / maxMbps).coerceIn(0f, 1f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(240.dp)
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val strokeWidth = 18.dp.toPx()
            val radius = (size.width - strokeWidth) / 2f
            val arcSize = Size(radius * 2f, radius * 2f)
            val topLeft = Offset(center.x - radius, center.y - radius)

            val startAngle = 135f
            val maxSweepAngle = 270f

            // Background Arc Track
            drawArc(
                color = Color(0xFF1E293B),
                startAngle = startAngle,
                sweepAngle = maxSweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active Speed Arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(CyberCyan, CyberEmerald, CyberRose)
                ),
                startAngle = startAngle,
                sweepAngle = maxSweepAngle * sweepFraction,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Needle Pointer
            val needleAngleRad = Math.toRadians((startAngle + maxSweepAngle * sweepFraction).toDouble())
            val needleLength = radius * 0.75f
            val needleEnd = Offset(
                x = center.x + (needleLength * cos(needleAngleRad)).toFloat(),
                y = center.y + (needleLength * sin(needleAngleRad)).toFloat()
            )

            drawLine(
                color = CyberCyan,
                start = center,
                end = needleEnd,
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawCircle(
                color = CyberCyan,
                radius = 8.dp.toPx(),
                center = center
            )
        }

        // Center Text
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (progress <= 0.6f) "DOWNLOAD" else "UPLOAD",
                style = MaterialTheme.typography.labelSmall,
                color = CyberTextSecondary,
                letterSpacing = 1.2.sp
            )

            Text(
                text = String.format("%.1f", animatedMbps),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = CyberTextPrimary,
                fontSize = 38.sp
            )

            Text(
                text = "Mbps",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = CyberCyan
            )
        }
    }
}
