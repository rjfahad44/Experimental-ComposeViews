package com.bitbytestudio.experimental_composeviews.ui.experiment.liquidGlassEffect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val rippleState = rememberGlassRippleState()

    GlassContainer(
        modifier = modifier,
        rippleState = rippleState
    ) {
        Column {
            content()
        }
    }
}