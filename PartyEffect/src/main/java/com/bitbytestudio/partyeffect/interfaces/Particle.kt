package com.bitbytestudio.partyeffect.interfaces

import com.bitbytestudio.partyeffect.models.Shape

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