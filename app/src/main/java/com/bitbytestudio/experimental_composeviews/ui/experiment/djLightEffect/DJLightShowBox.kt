package com.bitbytestudio.experimental_composeviews.ui.experiment.djLightEffect

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun DJLightShowBox(
    modifier: Modifier = Modifier,
    lightCount: Int = 3,
    beamColors: List<Pair<Color, Color>>? = null, // Use pair for distinct animations
    content: @Composable BoxScope.() -> Unit = {}
) {
    val lights = remember(lightCount, beamColors) {
        List(lightCount) { index ->
            val colors = beamColors?.getOrNull(index)
            DJLight(
                beamCount = 3 + Random.nextInt(3),
                baseAngle = Random.nextFloat() * 90f + 45f,
                color1 = colors?.first ?: randomColor(),
                color2 = colors?.second ?: randomColor(),
                speed = Random.nextInt(500, 1500),
                positionXRatio = (index + 1).toFloat() / (lightCount + 1)
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        lights.forEach { light ->
            AnimatedDJLightGroup(light)
        }
        content()
    }
}

@Composable
fun AnimatedDJLightGroup(light: DJLight) {
    val transition = rememberInfiniteTransition()

    val angle by transition.animateFloat(
        initialValue = light.baseAngle - 45f,
        targetValue = light.baseAngle + 45f,
        animationSpec = infiniteRepeatable(
            animation = tween(light.speed, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val color by transition.animateColor(
        initialValue = light.color1,
        targetValue = light.color2,
        animationSpec = infiniteRepeatable(
            animation = tween(light.speed, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    DJLightGroup(
        positionXRatio = light.positionXRatio,
        angle = angle,
        color = color,
        beamCount = light.beamCount
    )
}

@Composable
fun DJLightGroup(
    positionXRatio: Float,
    angle: Float,
    color: Color,
    beamCount: Int,
    beamLength: Float = 1000f,
    spreadAngle: Float = 35f,
    beamWidth: Float = 300f
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val origin = Offset(size.width * positionXRatio, 0f)
        val startAngle = angle - spreadAngle / 2
        val angleStep = spreadAngle / (beamCount - 1).coerceAtLeast(1)

        repeat(beamCount) { i ->
            val beamAngle = startAngle + i * angleStep
            val radians = Math.toRadians(beamAngle.toDouble())

            val leftX = origin.x + cos(radians - 0.1) * beamWidth
            val rightX = origin.x + cos(radians + 0.1) * beamWidth
            val bottomY = origin.y + sin(radians) * beamLength

            val path = Path().apply {
                moveTo(origin.x, origin.y)
                lineTo(leftX.toFloat(), bottomY.toFloat())
                lineTo(rightX.toFloat(), bottomY.toFloat())
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(color.copy(alpha = 0.5f), Color.Transparent),
                    startY = 0f,
                    endY = beamLength
                ),
                blendMode = BlendMode.SrcOver
            )
        }

        drawCircle(
            color = color,
            radius = 14f,
            center = origin,
            blendMode = BlendMode.SrcOver
        )
    }
}

data class DJLight(
    val beamCount: Int,
    val baseAngle: Float,
    val color1: Color,
    val color2: Color,
    val speed: Int,
    val positionXRatio: Float
)

fun randomColor(): Color {
    return Color(
        red = Random.nextFloat().coerceIn(0.4f, 1f),
        green = Random.nextFloat().coerceIn(0.4f, 1f),
        blue = Random.nextFloat().coerceIn(0.4f, 1f),
        alpha = 1f
    )
}





@Preview(showBackground = true)
@Composable
fun DJLightShowPreview() {
    val beamColors = listOf(
        Color.Red to Color.Yellow,
        Color.Cyan to Color.Blue,
        Color.Magenta to Color.Green
    )

    DJLightShowBox(
        lightCount = 3,
        beamColors = beamColors,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            "DJ Party!",
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.White.copy(alpha = 0.1f))
                .padding(16.dp),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}