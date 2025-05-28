package com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.models

import kotlin.time.Duration


public data class Emitter(
    val duration: Duration,
) {
    public fun max(amount: Int): EmitterConfig = EmitterConfig(this).max(amount)
    public fun perSecond(amount: Int): EmitterConfig = EmitterConfig(this).perSecond(amount)
}


public class EmitterConfig(
    emitter: Emitter,
) {
    public var emittingTime: Long = 0
    public var amountPerMs: Float = 0f

    init {
        this.emittingTime = emitter.duration.inWholeMilliseconds
    }


    public fun max(amount: Int): EmitterConfig {
        this.amountPerMs = (emittingTime / amount) / 1000f
        return this
    }


    public fun perSecond(amount: Int): EmitterConfig {
        this.amountPerMs = 1f / amount
        return this
    }
}