package com.bitbytestudio.experimental_composeviews.ui.experiment.curvedSlider

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CurvedSliderScreen() {
    var sliderValue by remember { mutableFloatStateOf(50f) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
        ,
        contentAlignment = Alignment.Center
    ) {
        CurvedSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            modifier = Modifier
                .height(500.dp)
                .width(200.dp)
        )
    }
}


@Composable
fun CurvedSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float> = 0f..100f,
    lineStroke: Stroke = Stroke(width = 15f, cap = StrokeCap.Round),
    thumbRadius: Dp = 22.dp,
    lineCurveBendY: Float = 100f,
    lineCurveBendX: Float = 35f,
    lineCurveShiftX: Float = 30f
) {
    var sliderHeight by remember { mutableFloatStateOf(0f) }
    val radiusPx = with(LocalDensity.current) { thumbRadius.toPx() }

    Box(modifier) {

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, offset ->
                            change.consume()

                            // Calculate value - allow dragging even at boundaries
                            val newY = change.position.y.coerceIn(radiusPx, sliderHeight - radiusPx)
                            val newValue = ((sliderHeight - newY) / sliderHeight * 100f).coerceIn(range.start, range.endInclusive)
                            onValueChange(newValue)
                        }
                    )
                }
        ) {
            sliderHeight = size.height
            val centerX = size.width * 0.50f       // left-aligned
            //val thumbY = sliderHeight - (value / 100f * sliderHeight)
            val thumbY = (sliderHeight - (value / 100f * sliderHeight)).coerceIn(radiusPx, sliderHeight - radiusPx)

            val gradient = Brush.verticalGradient(
                listOf(
                    Color(0xFFEB2D4C),   // red top
                    Color(0xFF376BFF)    // blue bottom
                )
            )

            // Clamp for curve safety
            val topLimit = lineStroke.width
            val bottomLimit = sliderHeight - lineStroke.width

            // Shrink curve near edges (half curve at top/bottom)
            val curveFactor = adaptiveCurveFactor(thumbY, sliderHeight, radiusPx)

            val bendY = lineCurveBendY * curveFactor
            val bendX = lineCurveBendX * curveFactor

            val topCurveY1 = (thumbY - bendY).coerceAtLeast(topLimit)
            val topCurveY2 = (thumbY - bendX).coerceAtLeast(topLimit)

            val bottomCurveY2 = (thumbY + bendX).coerceAtMost(bottomLimit)
            val bottomCurveY1 = (thumbY + bendY).coerceAtMost(bottomLimit)


            // -------- Curved path around thumb --------
            val curvedPath = Path().apply {
                moveTo(centerX, topLimit)
                lineTo(centerX, topCurveY1)

                cubicTo(
                    centerX, topCurveY2,
                    centerX - lineCurveShiftX, topCurveY2,
                    centerX - lineCurveShiftX, thumbY
                )
                cubicTo(
                    centerX - lineCurveShiftX, bottomCurveY2,
                    centerX, bottomCurveY2,
                    centerX, bottomCurveY1
                )

                lineTo(centerX, bottomLimit)
            }


            // -------- Line stroke --------
            drawPath(
                path = curvedPath,
                brush = gradient,
                style = lineStroke
            )

            // -------- Outer white glow --------
            drawCircle(
                color = Color.White.copy(alpha = 0.4f),
                radius = radiusPx * 1.0f,
                center = Offset(centerX + lineCurveShiftX, thumbY)
            )

            // -------- Thumb main circle --------
            drawCircle(
                color = Color(0xFF376BFF),
                radius = radiusPx,
                center = Offset(centerX + lineCurveShiftX, thumbY)
            )

            // -------- Center white dot --------
            drawCircle(
                color = Color.White,
                radius = 5.dp.toPx(),
                center = Offset(centerX + lineCurveShiftX, thumbY)
            )
        }

        // ================== Percentage Labels ====================
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val selectedTick = (ceil(value)  / 10).roundToInt() * 10
            for (i in 100 downTo 10 step 10) {
                val selected = (i == selectedTick)
                Text(
                    text = "$i%",
                    fontSize = if (selected) 22.sp else 16.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) Color(0xFF376BFF) else Color(0xFF8A8A8A)
                )
            }
        }
    }
}


private fun adaptiveCurveFactor(thumbY: Float, height: Float, radius: Float): Float {
    val minDist = radius * 2.5f  // distance from edges
    val minFactor = .5f        // 👈 curve will shrink to HALF, not zero

    return when {
        thumbY < minDist -> (thumbY / minDist).coerceAtLeast(minFactor)
        thumbY > height - minDist -> ((height - thumbY) / minDist).coerceAtLeast(minFactor)
        else -> 1f
    }
}
