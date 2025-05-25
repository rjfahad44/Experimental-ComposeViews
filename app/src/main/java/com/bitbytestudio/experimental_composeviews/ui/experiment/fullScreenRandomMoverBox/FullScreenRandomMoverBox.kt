package com.bitbytestudio.experimental_composeviews.ui.experiment.fullScreenRandomMoverBox


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun FullScreenRandomMoverBox(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1000,
    content: @Composable BoxScope.() -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    var screenWidthPx by remember { mutableFloatStateOf(0f) }
    var screenHeightPx by remember { mutableFloatStateOf(0f) }

    var contentWidthPx by remember { mutableFloatStateOf(0f) }
    var contentHeightPx by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                screenWidthPx = coordinates.size.width.toFloat()
                screenHeightPx = coordinates.size.height.toFloat()
            }
    ) {
        LaunchedEffect(screenWidthPx, screenHeightPx, contentWidthPx, contentHeightPx) {
            if (screenWidthPx == 0f || screenHeightPx == 0f || contentWidthPx == 0f || contentHeightPx == 0f) return@LaunchedEffect

            while (true) {
                val maxX = screenWidthPx - contentWidthPx
                val maxY = screenHeightPx - contentHeightPx

                val targetX = (0..maxX.toInt()).random().toFloat()
                val targetY = (0..maxY.toInt()).random().toFloat()

                launch {
                    offsetX.animateTo(
                        targetValue = targetX,
                        animationSpec = tween(durationMillis, easing = LinearEasing)
                    )
                }

                launch {
                    offsetY.animateTo(
                        targetValue = targetY,
                        animationSpec = tween(durationMillis, easing = LinearEasing)
                    )
                }

                delay(durationMillis.toLong())
            }
        }

        Box(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    contentWidthPx = coordinates.size.width.toFloat()
                    contentHeightPx = coordinates.size.height.toFloat()
                }
                .offset {
                    IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt())
                },
            content = content
        )
    }
}