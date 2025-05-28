package com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.models

public sealed class Position {
    public data class Absolute(val x: Float, val y: Float) : Position() {
        public fun between(value: Absolute): Position = Between(this, value)
    }
    public data class Relative(val x: Double, val y: Double) : Position() {
        public fun between(value: Relative): Position = Between(this, value)
    }
    public data class Between(val min: Position, val max: Position) : Position()
}
