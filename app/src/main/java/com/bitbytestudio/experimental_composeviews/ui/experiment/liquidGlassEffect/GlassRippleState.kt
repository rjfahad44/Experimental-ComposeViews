package com.bitbytestudio.experimental_composeviews.ui.experiment.liquidGlassEffect

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

@Stable
class GlassRippleState {
    var touch by mutableStateOf(Offset.Zero)
    var ripple by mutableFloatStateOf(0f)
    var time by mutableFloatStateOf(0f)

    fun trigger(offset: Offset) {
        touch = offset
        ripple = 1f
    }

    fun update(delta: Float) {
        time += delta
        ripple *= 0.96f // decay
    }
}