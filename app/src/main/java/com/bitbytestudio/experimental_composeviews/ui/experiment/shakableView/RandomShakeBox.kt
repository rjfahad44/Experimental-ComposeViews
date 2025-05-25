package com.bitbytestudio.experimental_composeviews.ui.experiment.shakableView

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RandomShakeBox(
    modifier: Modifier = Modifier,
    shakeRange: Int = 10,
    durationMillis: Int = 200,
    content: @Composable BoxScope.() -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            val randomX = (-shakeRange..shakeRange).random().toFloat()
            val randomY = (-shakeRange..shakeRange).random().toFloat()

            launch {
                offsetX.animateTo(
                    targetValue = randomX,
                    animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
                )
            }
            launch {
                offsetY.animateTo(
                    targetValue = randomY,
                    animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
                )
            }
            delay(durationMillis.toLong())
        }
    }

    Box(
        modifier = modifier.offset(x = offsetX.value.dp, y = offsetY.value.dp),
        content = content
    )
}

