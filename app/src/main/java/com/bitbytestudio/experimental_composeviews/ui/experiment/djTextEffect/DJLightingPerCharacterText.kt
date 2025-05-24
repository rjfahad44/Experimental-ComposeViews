package com.bitbytestudio.experimental_composeviews.ui.experiment.djTextEffect

import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun DJLightingPerCharacterText(
    text: String,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    val colorOptions = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Magenta, Color.Yellow, Color.Cyan, Color.White
    )

    Row(modifier = modifier.basicMarquee(
        animationMode = MarqueeAnimationMode.Immediately,
        spacing = MarqueeSpacing(spacing = 30.dp)
    )) {
        text.forEachIndexed { index, char ->
            var color by remember { mutableStateOf(colorOptions.random()) }
            // Launch color change for each character independently
            LaunchedEffect(key1 = index) {
                while (true) {
                    delay(300L + index * 100L) // different delay per char
                    color = colorOptions.random()
                }
            }

            Text(
                text = char.toString(),
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
