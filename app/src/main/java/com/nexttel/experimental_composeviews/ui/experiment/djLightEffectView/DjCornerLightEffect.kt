package com.nexttel.experimental_composeviews.ui.experiment.djLightEffectView

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DjCornerLightEffect(
    modifier: Modifier = Modifier,
    beamColorList: List<Color> = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta),
    beamWidth: Float = 5f,
    beamCount: Int = 4,
    beamLengthFactor: Float = 1.5f,
    rotationSpeed: Float = 30f,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "corner_light_anim")
    val rotationAngle = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((360 / rotationSpeed * 1000).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "corner_rotation"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val size = size
            val corners = listOf(
                Offset(0f, 0f), // Top-left
                Offset(size.width, 0f), // Top-right
                Offset(0f, size.height), // Bottom-left
                Offset(size.width, size.height) // Bottom-right
            )

            for ((index, corner) in corners.withIndex()) {
                for (i in 0 until beamCount) {
                    val angleOffset = (360f / beamCount) * i + rotationAngle.value + (index * 90)
                    val radians = Math.toRadians(angleOffset.toDouble())
                    val radius = size.minDimension * beamLengthFactor

                    val endX = corner.x + cos(radians) * radius
                    val endY = corner.y + sin(radians) * radius

                    val color = beamColorList[(i + index) % beamColorList.size]

                    drawLine(
                        color = color,
                        start = corner,
                        end = Offset(endX.toFloat(), endY.toFloat()),
                        strokeWidth = beamWidth,
                        alpha = 0.4f,
                        cap = StrokeCap.Round
                    )

                    // Light glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(color.copy(alpha = 0.3f), Color.Transparent),
                            center = corner,
                            radius = size.minDimension * 0.25f
                        ),
                        center = corner,
                        radius = size.minDimension * 0.25f
                    )
                }
            }
        }

        // ✅ Your custom content composable inside the light box
        content()
    }
}
