package com.bitbytestudio.partyeffect

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.bitbytestudio.partyeffect.interfaces.CoreRect
import com.bitbytestudio.partyeffect.interfaces.Particle
import com.bitbytestudio.partyeffect.interfaces.draw
import com.bitbytestudio.partyeffect.models.Party
import com.bitbytestudio.partyeffect.models.PartySystem

@Composable
public fun PartyCanvas(
    modifier: Modifier = Modifier,
    parties: List<Party>,
    onParticleSystemEnded: (PartySystem, Int) -> Unit = { _, _ -> },
) {
    lateinit var partySystems: List<PartySystem>

    val particles = remember { mutableStateOf(emptyList<Particle>()) }
    val frameTime = remember { mutableLongStateOf(0L) }
    val drawArea = remember { mutableStateOf(CoreRect.CoreRectImpl()) }
    val density = LocalDensity.current

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        frameTime.value = 0L
    }
    LaunchedEffect(Unit) {
        partySystems = parties.map {
            PartySystem(
                party = storeImages(it),
                pixelDensity = density.density,
            )
        }

        while (true) {
            withInfiniteAnimationFrameMillis { frameMs ->
                // Calculate time between frames, fallback to 0 when previous frame doesn't exist
                val deltaMs = if (frameTime.value > 0) (frameMs - frameTime.value) else 0
                frameTime.value = frameMs

                particles.value = partySystems.map { particleSystem ->
                    val totalTimeRunning = getTotalTimeRunning(particleSystem.createdAt)
                    // Do not start particleSystem yet if totalTimeRunning is below delay
                    if (totalTimeRunning < particleSystem.party.delay) return@map listOf()

                    if (particleSystem.isDoneEmitting()) {
                        onParticleSystemEnded(
                            particleSystem,
                            partySystems.count { !it.isDoneEmitting() },
                        )
                    }

                    particleSystem.render(deltaMs.div(1000f), drawArea.value)
                }.flatten()
            }
        }
    }

    Canvas(
        modifier = modifier.onGloballyPositioned {
            drawArea.value = CoreRect.CoreRectImpl(
                x = 0f,
                y = 0f,
                width = it.size.width.toFloat(),
                height = it.size.height.toFloat()
            )
        },
        onDraw = {
            particles.value.forEach { particle ->
                withTransform({
                    rotate(
                        degrees = particle.rotation,
                        pivot = Offset(
                            x = particle.x + (particle.width / 2),
                            y = particle.y + (particle.height / 2),
                        ),
                    )
                    scale(
                        scaleX = particle.scaleX,
                        scaleY = 1f,
                        pivot = Offset(particle.x + (particle.width / 2), particle.y),
                    )
                }) {
                    particle.shape.draw(
                        drawScope = this,
                        particle = particle,
                    )
                }
            }
        },
    )
}

internal fun storeImages(
    party: Party,
): Party {
    val transformedShapes = party.shapes.map { shape -> shape }
    return party.copy(shapes = transformedShapes)
}

internal fun getTotalTimeRunning(startTime: Long): Long {
    val currentTime = System.currentTimeMillis()
    return (currentTime - startTime)
}