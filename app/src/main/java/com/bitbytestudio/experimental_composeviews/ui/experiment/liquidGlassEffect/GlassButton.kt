package com.bitbytestudio.experimental_composeviews.ui.experiment.liquidGlassEffect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit
) {
    val rippleState = rememberGlassRippleState()

    GlassContainer(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        rippleState = rippleState
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text, color = Color.White)
        }
    }
}