package com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.interfaces

import com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.models.Shape

internal data class Particle(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val color: Int,
    val rotation: Float,
    val scaleX: Float,
    val shape: Shape,
    val alpha: Int,
)