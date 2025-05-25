package com.bitbytestudio.utils

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

object DynamicUniqueColorGenerator {
    private var hueStep = 0
    private val usedColors = mutableSetOf<Color>()

    fun nextColor(): Color {
        val hue = (hueStep * 137.508).mod(360.0)
        hueStep++

        val color = hslToColor(hue.toFloat(), 0.6f, 0.5f)

        if (usedColors.contains(color)) return nextColor()
        usedColors.add(color)
        return color
    }

    private fun hslToColor(h: Float, s: Float, l: Float): Color {
        val c = (1 - kotlin.math.abs(2 * l - 1)) * s
        val x = c * (1 - kotlin.math.abs((h / 60) % 2 - 1))
        val m = l - c / 2

        val (r1, g1, b1) = when {
            h < 60 -> Triple(c, x, 0f)
            h < 120 -> Triple(x, c, 0f)
            h < 180 -> Triple(0f, c, x)
            h < 240 -> Triple(0f, x, c)
            h < 300 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        val r = ((r1 + m) * 255).roundToInt()
        val g = ((g1 + m) * 255).roundToInt()
        val b = ((b1 + m) * 255).roundToInt()

        return Color(r, g, b)
    }
}

