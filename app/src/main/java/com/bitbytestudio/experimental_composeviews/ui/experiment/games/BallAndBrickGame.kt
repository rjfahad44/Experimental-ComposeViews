package com.bitbytestudio.experimental_composeviews.ui.experiment.games

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BallAndBrickGame(modifier: Modifier = Modifier) {
    var score by remember { mutableIntStateOf(0) }
    var ballColor by remember { mutableStateOf(Color.Blue) }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val screenWidthPx = constraints.maxWidth.toFloat()
        val screenHeightPx = constraints.maxHeight.toFloat()

        val ballRadiusPx = with(density) { 15.dp.toPx() }

        var ballPosition by remember { mutableStateOf(Offset(screenWidthPx / 2, screenHeightPx / 2)) }
        var velocity by remember { mutableStateOf(Offset(3f, 2f)) }

        // Brick size
        val brickLength = with(density) { 50.dp.toPx() }  // একেকটা brick এর দৈর্ঘ্য
        val brickThickness = with(density) { 20.dp.toPx() }

        val brickColors = listOf(Color.Red, Color.Green, Color.Yellow, Color.Blue)

        // Drag offset (snake-like scroll effect)
        var snakeOffset by remember { mutableStateOf(0f) }

        // Total perimeter
        val perimeter = 2 * (screenWidthPx + screenHeightPx)

        // Ball movement
        LaunchedEffect(Unit) {
            while (true) {
                delay(16)
                ballPosition += velocity

                if (ballPosition.x <= ballRadiusPx || ballPosition.x >= screenWidthPx - ballRadiusPx) {
                    velocity = Offset(-velocity.x, velocity.y)
                    ballColor = brickColors.random()
                    score++
                }
                if (ballPosition.y <= ballRadiusPx || ballPosition.y >= screenHeightPx - ballRadiusPx) {
                    velocity = Offset(velocity.x, -velocity.y)
                    ballColor = brickColors.random()
                    score++
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Canvas(modifier = Modifier.matchParentSize()) {
                var colorIndex = 0

                // কত brick দরকার হবে
                val brickCount = (perimeter / brickLength).toInt() + 5

                for (i in 0 until brickCount) {
                    val dist = (i * brickLength + snakeOffset).mod(perimeter)

                    val top = 0f
                    val left = 0f
                    val right = size.width
                    val bottom = size.height

                    val pos: Offset
                    val sizeRect: Size

                    when {
                        // Top side
                        dist < right -> {
                            pos = Offset(left + dist, top)
                            sizeRect = Size(brickLength, brickThickness)
                        }
                        // Right side
                        dist < right + bottom -> {
                            pos = Offset(right - brickThickness, dist - right)
                            sizeRect = Size(brickThickness, brickLength)
                        }
                        // Bottom side
                        dist < right + bottom + right -> {
                            val d = dist - (right + bottom)
                            pos = Offset(right - d, bottom - brickThickness)
                            sizeRect = Size(brickLength, brickThickness)
                        }
                        // Left side
                        else -> {
                            val d = dist - (right + bottom + right)
                            pos = Offset(left, bottom - d)
                            sizeRect = Size(brickThickness, brickLength)
                        }
                    }

                    drawRoundRect(
                        color = brickColors[colorIndex % brickColors.size],
                        topLeft = pos,
                        size = sizeRect,
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    colorIndex++
                }

                // Ball
                drawCircle(
                    color = ballColor,
                    center = ballPosition,
                    radius = ballRadiusPx
                )
            }

            // Snake drag effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 0f)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            // X drag = horizontal move, Y drag = vertical move → combine
                            snakeOffset += (dragAmount.x + dragAmount.y)
                            change.consume()
                        }
                    }
            )

            // Score
            Text(
                text = "Score: $score",
                modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}
