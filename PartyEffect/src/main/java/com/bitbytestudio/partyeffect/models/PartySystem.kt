package com.bitbytestudio.partyeffect.models

import com.bitbytestudio.partyeffect.interfaces.BaseEmitter
import com.bitbytestudio.partyeffect.interfaces.CoreRect
import com.bitbytestudio.partyeffect.interfaces.Particle
import com.bitbytestudio.partyeffect.interfaces.PartyCore
import com.bitbytestudio.partyeffect.interfaces.PartyEmitter


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