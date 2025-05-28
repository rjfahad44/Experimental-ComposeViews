package com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.models

import com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.interfaces.BaseEmitter
import com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.interfaces.PartyCore
import com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.interfaces.CoreRect
import com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.interfaces.Particle
import com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.interfaces.PartyEmitter


public class PartySystem(
    public val party: Party,
    public val createdAt: Long = System.currentTimeMillis(),
    pixelDensity: Float,
) {
    private var enabled = true

    private var emitter: BaseEmitter = PartyEmitter(party.emitter, pixelDensity)

    private val activeParticles = mutableListOf<PartyCore>()

    internal fun render(
        deltaTime: Float,
        drawArea: CoreRect,
    ): List<Particle> {
        if (enabled) {
            activeParticles.addAll(emitter.createConfetti(deltaTime, party, drawArea))
        }

        activeParticles.forEach { it.render(deltaTime, drawArea) }

        activeParticles.removeAll { it.isDead() }

        return activeParticles.filter { it.drawParticle }.map { it.toParticle() }
    }

    internal fun isDoneEmitting(): Boolean = (emitter.isFinished() && activeParticles.isEmpty()) || (!enabled && activeParticles.isEmpty())

    internal fun getActiveParticleAmount() = activeParticles.size
}


internal fun PartyCore.toParticle(): Particle {
    return Particle(
        location.x,
        location.y,
        width,
        width,
        alphaColor,
        rotation,
        scaleX,
        shape,
        alpha,
    )
}