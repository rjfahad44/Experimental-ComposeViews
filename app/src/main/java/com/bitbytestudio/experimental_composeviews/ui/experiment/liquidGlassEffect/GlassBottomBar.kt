package com.bitbytestudio.experimental_composeviews.ui.experiment.liquidGlassEffect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassBottomBar(
    items: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    val rippleState = rememberGlassRippleState()

    GlassContainer(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        rippleState = rippleState
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                Text(
                    text = item,
                    color = if (index == selectedIndex)
                        Color.White
                    else
                        Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.clickable {
                        onSelected(index)
                    }
                )
            }
        }
    }
}