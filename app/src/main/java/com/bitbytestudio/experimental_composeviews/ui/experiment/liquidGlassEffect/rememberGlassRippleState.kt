package com.bitbytestudio.experimental_composeviews.ui.experiment.liquidGlassEffect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos

@Composable
fun rememberGlassRippleState(): GlassRippleState {
    val state = remember { GlassRippleState() }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                state.update(0.016f)
            }
        }
    }
    return state
}