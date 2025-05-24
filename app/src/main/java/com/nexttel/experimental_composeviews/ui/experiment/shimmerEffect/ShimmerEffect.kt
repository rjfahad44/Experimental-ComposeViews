package com.nexttel.experimental_composeviews.ui.experiment.shimmerEffect

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.tan


fun Modifier.shimmerEffect(
    durationMillis: Int = 2000,
    shimmerWidthDp: Dp = 200.dp,
    angleInDegrees: Float = 20f,
    shimmerColors: List<Color> =  listOf(
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.9f),
        Color.LightGray.copy(alpha = 0.3f)
    )
): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "ShimmerAnimation")
    val animatedValue = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerAnim"
    )

    val density = LocalDensity.current
    val shimmerWidthPx = with(density) { shimmerWidthDp.toPx() }
    val angleInRad = Math.toRadians(angleInDegrees.toDouble())

    val xOffset = animatedValue.value * shimmerWidthPx * 2

    val startX = xOffset - shimmerWidthPx
    val endX = xOffset

    val startY = 0f
    val endY = tan(angleInRad).toFloat() * shimmerWidthPx

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(startX, startY),
        end = Offset(endX, endY)
    )

    background(brush)
}

