package com.nexttel.experimental_composeviews.ui.experiment.djLightEffect

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
import androidx.compose.ui.graphics.Path
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun DjLightStageEffect(
    modifier: Modifier = Modifier,
    beamColorList: List<Color> = listOf(
        Color(0xFF00FFFF),
        Color(0xFFFF00FF),
        Color(0xFFFFFF00),
        Color(0xFF00FF00),
        Color(0xFFFF0000),
        Color(0xFF0000FF)
    ),
    beamWidth: Float = 60f,
    beamLengthFactor: Float = 1.8f,
    beamCount: Int = 3,
    rotationRange: Float = 20f,
    rotationSpeed: Float = 25f,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dj_real_light")
    val rotationAnim = infiniteTransition.animateFloat(
        initialValue = -rotationRange,
        targetValue = rotationRange,
        animationSpec = infiniteRepeatable(
            animation = tween((1000 * 60 / rotationSpeed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "light_rotation_anim"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val canvasSize = size
            val beamLength = canvasSize.minDimension * beamLengthFactor

            val topLeft = Offset(0f, 0f)
            val topRight = Offset(canvasSize.width, 0f)

            val drawBeam = { start: Offset, angle: Float, color: Color ->
                val rad = Math.toRadians(angle.toDouble())
                val direction = Offset(cos(rad).toFloat(), sin(rad).toFloat())
                val end = start + direction * beamLength

                val distance = (end - start).getDistance()
                val normalizedDistance = (distance / beamLength).coerceIn(0f, 1f)

                // Dynamic widths
                val startWidth = beamWidth * 0.4f
                val endWidth = beamWidth * 1.4f

                // Perpendicular vector for width offset
                val perpendicular = Offset(-direction.y, direction.x)

                val startLeft = start + perpendicular * (startWidth / 2)
                val startRight = start - perpendicular * (startWidth / 2)
                val endLeft = end + perpendicular * (endWidth / 2)
                val endRight = end - perpendicular * (endWidth / 2)

                val path = Path().apply {
                    moveTo(startLeft.x, startLeft.y)
                    lineTo(endLeft.x, endLeft.y)
                    lineTo(endRight.x, endRight.y)
                    lineTo(startRight.x, startRight.y)
                    close()
                }

                // Draw trapezoid beam
                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = listOf(color.copy(alpha = 0.4f), color.copy(alpha = 0f)),
                        start = start,
                        end = end
                    )
                )

                // Optional: glowing end circle
                val minRadius = beamWidth * 0.6f
                val maxRadius = beamWidth * 2.5f
                val dynamicGlowRadius = minRadius + (maxRadius - minRadius) * normalizedDistance

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color.copy(alpha = 0.45f), Color.Transparent),
                        center = end,
                        radius = dynamicGlowRadius
                    ),
                    center = end,
                    radius = dynamicGlowRadius
                )
            }



            for (i in 0 until beamCount) {
                val spacing = 12f // Angle spacing between beams
                val baseAngle = rotationAnim.value
                val spreadAngle = baseAngle + (i - beamCount / 2f) * spacing

                val color = beamColorList[i % beamColorList.size]

                // Left side beam
                drawBeam(topLeft, 45f + spreadAngle, color)

                // Right side beam
                drawBeam(topRight, 135f - spreadAngle, color)
            }
        }

        // Content inside stage
        content()
    }
}

