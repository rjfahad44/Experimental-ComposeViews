package com.nexttel.experimental_composeviews.utils

import androidx.compose.ui.graphics.Color

fun Int.colorFromIndex(): Color {
    val colors = listOf(
        Color(0xFF2196F3),
        Color(0xFFFF9800),
        Color(0xFF4CAF50),
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFFFF5722)
    )
    return colors[this % colors.size]
}