package com.bitbytestudio.experimental_composeviews.ui.experiment.games

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitbytestudio.experimental_composeviews.R
import kotlinx.coroutines.delay

@Composable
fun CarGameScreen() {
    val density = LocalDensity.current

    // Car dimensions
    val carWidthDp = 60.dp
    val carHeightDp = 80.dp
    val bottomPaddingDp = 50.dp

    val carWidthPx = with(density) { carWidthDp.toPx() }
    val carHeightPx = with(density) { carHeightDp.toPx() }
    val bottomPaddingPx = with(density) { bottomPaddingDp.toPx() }

    var screenWidth by remember { mutableFloatStateOf(0f) }
    var screenHeight by remember { mutableFloatStateOf(0f) }

    val playerCar = remember { mutableStateOf(Offset.Zero) }
    val enemyCars = remember { mutableStateListOf<Offset>() }

    var gameOver by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var level by remember { mutableIntStateOf(1) }
    var speed by remember { mutableFloatStateOf(6f) }

    val playerCarImage = painterResource(id = R.drawable.car_2)
    val enemyCarImage = painterResource(id = R.drawable.car_1)

    val laneCount = 4
    val laneX = remember(screenWidth) {
        if (screenWidth == 0f) return@remember emptyList()
        val laneWidth = screenWidth / laneCount
        List(laneCount) { it * laneWidth + (laneWidth - carWidthPx) / 2 }
    }

    // UI Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .onGloballyPositioned {
                val size = it.size
                val width = size.width.toFloat()
                val height = size.height.toFloat()

                if (screenWidth != width || screenHeight != height) {
                    screenWidth = width
                    screenHeight = height
                    playerCar.value = Offset(
                        width / 2 - carWidthPx / 2,
                        height - carHeightPx - bottomPaddingPx
                    )
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val x = change.position.x.coerceIn(0f, screenWidth - carWidthPx)
                    playerCar.value = playerCar.value.copy(x = x)
                }
            }
    ) {
        // Road
        RoadMap(screenWidth, screenHeight, laneCount)

        // Player Car
        Image(
            painter = playerCarImage,
            contentDescription = "Player",
            modifier = Modifier
                .size(carWidthDp, carHeightDp)
                .offset {
                    IntOffset(playerCar.value.x.toInt(), playerCar.value.y.toInt())
                }
        )

        // Enemy Cars
        enemyCars.forEach { enemy ->
            Image(
                painter = enemyCarImage,
                contentDescription = "Enemy",
                modifier = Modifier
                    .size(carWidthDp, carHeightDp)
                    .offset {
                        IntOffset(enemy.x.toInt(), enemy.y.toInt())
                    }
            )
        }

        // Score & Level
        Text(
            text = "Score: $score",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
        Text(
            text = "Level: $level",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        // Game Over Dialog
        if (gameOver) {
            GameOverDialog(onRetry = {
                gameOver = false
                score = 0
                level = 1
                speed = 6f
                enemyCars.clear()
                if (laneX.isNotEmpty()) {
                    repeat(3) {
                        enemyCars.add(spawnEnemyCar(laneX, -carHeightPx * it))
                    }
                }
            })
        }

        // Game Loop
        LaunchedEffect(screenWidth, screenHeight, gameOver) {
            if (screenWidth > 0f && screenHeight > 0f && !gameOver) {
                while (true) {
                    withFrameNanos {
                        if (gameOver || laneX.isEmpty()) return@withFrameNanos

                        val updatedEnemies = mutableListOf<Offset>()
                        enemyCars.forEach { car ->
                            val newY = car.y + speed
                            if (newY > screenHeight) {
                                score++
                                updatedEnemies.add(spawnEnemyCar(laneX))
                            } else {
                                updatedEnemies.add(Offset(car.x, newY))
                            }
                        }

                        enemyCars.clear()
                        enemyCars.addAll(updatedEnemies)

                        val playerRect = Rect(playerCar.value, Size(carWidthPx, carHeightPx))
                        if (enemyCars.any {
                                val enemyRect = Rect(it, Size(carWidthPx, carHeightPx))
                                playerRect.overlaps(enemyRect)
                            }) {
                            gameOver = true
                        }
                    }
                }
            }
        }

        // Increase Speed Over Time
        LaunchedEffect(Unit) {
            while (true) {
                delay(5000)
                if (!gameOver) speed += 0.5f
            }
        }

        // Level Up
        LaunchedEffect(score) {
            if (score > 0 && score % 10 == 0 && laneX.isNotEmpty()) {
                level++
                if (enemyCars.size < 10) {
                    enemyCars.add(spawnEnemyCar(laneX))
                }
            }
        }

        // Initial spawn
        LaunchedEffect(laneX) {
            if (laneX.isNotEmpty() && enemyCars.isEmpty()) {
                repeat(3) {
                    enemyCars.add(spawnEnemyCar(laneX, -carHeightPx * it))
                }
            }
        }
    }
}

@Composable
fun GameOverDialog(onRetry: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0x99000000)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("💥 Game Over", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Tap to Retry",
                fontSize = 20.sp,
                color = Color.Yellow,
                modifier = Modifier
                    .clickable { onRetry() }
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun RoadMap(screenWidth: Float, screenHeight: Float, laneCount: Int) {
    val laneWidth = screenWidth / laneCount
    Canvas(Modifier.fillMaxSize()) {
        drawRect(Color.DarkGray, size = size)
        repeat(laneCount - 1) { i ->
            val x = laneWidth * (i + 1)
            drawLine(
                color = Color.White,
                start = Offset(x, 0f),
                end = Offset(x, screenHeight),
                strokeWidth = 6f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f))
            )
        }
    }
}

fun spawnEnemyCar(laneX: List<Float>, startY: Float = -150f): Offset {
    val x = laneX.random()
    return Offset(x, startY)
}






