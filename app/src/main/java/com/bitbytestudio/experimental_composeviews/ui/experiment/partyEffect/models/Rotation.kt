package com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.models

public data class Rotation(
    val enabled: Boolean = true,
    val speed: Float = 1f,
    val variance: Float = 0.5f,
    val multiplier2D: Float = 8f,
    val multiplier3D: Float = 1.5f,
) {
    public companion object {
        public fun enabled(): Rotation = Rotation(enabled = true)

        public fun disabled(): Rotation = Rotation(enabled = false)
    }
}