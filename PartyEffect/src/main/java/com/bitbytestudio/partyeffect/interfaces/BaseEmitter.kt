package com.bitbytestudio.partyeffect.interfaces

import com.bitbytestudio.partyeffect.models.Party

internal abstract class BaseEmitter {

    abstract fun createConfetti(
        deltaTime: Float,
        party: Party,
        drawArea: CoreRect,
    ): List<PartyCore>

    abstract fun isFinished(): Boolean
}