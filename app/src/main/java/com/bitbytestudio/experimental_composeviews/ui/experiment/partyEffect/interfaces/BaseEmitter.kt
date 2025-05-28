package com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.interfaces

import com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.models.Party

internal abstract class BaseEmitter {

    abstract fun createConfetti(
        deltaTime: Float,
        party: Party,
        drawArea: CoreRect,
    ): List<PartyCore>

    abstract fun isFinished(): Boolean
}