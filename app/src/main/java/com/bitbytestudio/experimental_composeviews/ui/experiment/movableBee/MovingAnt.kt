package com.bitbytestudio.experimental_composeviews.ui.experiment.movableBee

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.random.Random


@Composable
fun MovableBee(
    beeCount: Int = 1,
    beeSize: Dp = 30.dp,
    beeSpeedMillis: Int = 1000,
    beePainter: Painter,
    content: @Composable BoxScope.() -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Your content below bees
        content()

        repeat(beeCount) { index ->
            key(index) {
                val x = remember { Animatable(0f) }
                val y = remember { Animatable(0f) }
                var rotationAngle by remember { mutableFloatStateOf(0f) }

                LaunchedEffect(Unit) {
                    with(density) {
                        val maxX = screenWidth.toPx() - beeSize.toPx()
                        val maxY = screenHeight.toPx() - beeSize.toPx()

                        while (true) {
                            val targetX = Random.nextFloat() * maxX
                            val targetY = Random.nextFloat() * maxY

                            val dx = targetX - x.value
                            val dy = targetY - y.value
                            rotationAngle = Math.toDegrees(atan2(dy, dx).toDouble()).toFloat()

                            // Animate both axis together and wait till both finish
                            val xAnim = launch {
                                x.animateTo(targetX, tween(beeSpeedMillis))
                            }
                            val yAnim = launch {
                                y.animateTo(targetY, tween(beeSpeedMillis))
                            }

                            xAnim.join()
                            yAnim.join()

                            // Immediately start next move without delay
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .offset { IntOffset(x.value.toInt(), y.value.toInt()) }
                        .size(beeSize)
                        .rotate(rotationAngle + 90)
                ) {
                    Image(
                        painter = beePainter,
                        contentDescription = "Bee",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

