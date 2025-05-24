package com.nexttel.experimental_composeviews.utils

import androidx.compose.ui.graphics.Color

fun Int.colorFromIndex(): Color {
    val colors = listOf(
        Color(0xFF2196F3), // Blue
        Color(0xFFFF9800), // Orange
        Color(0xFF4CAF50), // Green
        Color(0xFFE91E63), // Pink
        Color(0xFF9C27B0), // Purple
        Color(0xFFFF5722)  // Deep Orange
    )
    return colors[this % colors.size]
}