package com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.models


public data class Party(
    val angle: Int = 0,
    val spread: Int = 360,
    val speed: Float = 30f,
    val maxSpeed: Float = 0f,
    val damping: Float = 0.9f,
    val size: List<Size> = listOf(Size.Companion.SMALL, Size.Companion.MEDIUM, Size.Companion.LARGE),
    val colors: List<Int> = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
    val shapes: List<Shape> = listOf(Shape.Square, Shape.Circle),
    val timeToLive: Long = 2000,
    val fadeOutEnabled: Boolean = true,
    val fadeOutDuration: Long = 850,
    val position: Position = Position.Relative(0.5, 0.5),
    val delay: Int = 0,
    val rotation: Rotation = Rotation(),
    val emitter: EmitterConfig,
)