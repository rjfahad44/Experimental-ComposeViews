package com.bitbytestudio.partyeffect.interfaces


import com.bitbytestudio.partyeffect.models.EmitterConfig
import com.bitbytestudio.partyeffect.models.Party
import com.bitbytestudio.partyeffect.models.Position
import com.bitbytestudio.partyeffect.models.Rotation
import com.bitbytestudio.partyeffect.models.Shape
import com.bitbytestudio.partyeffect.models.Size
import com.bitbytestudio.partyeffect.models.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

internal class PartyEmitter(
    private val emitterConfig: EmitterConfig,
    private val pixelDensity: Float,
    private val random: Random = Random.Default,
) : BaseEmitter() {
    // Keeping count of how many particles are created whilst running the emitter
    private var particlesCreated = 0

    private var elapsedTime: Float = 0f

    private var createParticleMs: Float = 0f

    override fun createConfetti(
        deltaTime: Float,
        party: Party,
        drawArea: CoreRect,
    ): List<PartyCore> {
        createParticleMs += deltaTime

        // Initial deltaTime can't be higher than the emittingTime, if so calculate
        // amount of particles based on max emittingTime
        val emittingTime = emitterConfig.emittingTime / 1000f
        if (elapsedTime == 0f && deltaTime > emittingTime) {
            createParticleMs = emittingTime
        }

        var particles = listOf<PartyCore>()

        // Check if particle should be created
        if (createParticleMs >= emitterConfig.amountPerMs && !isTimeElapsed()) {
            // Calculate how many particle  to create in the elapsed time
            val amount: Int = (createParticleMs / emitterConfig.amountPerMs).toInt()

            particles = (1..amount).map { createParticle(party, drawArea) }

            // Reset timer and add left over time for next cycle
            createParticleMs %= emitterConfig.amountPerMs
        }

        elapsedTime += deltaTime * 1000
        return particles
    }


    private fun createParticle(
        party: Party,
        drawArea: CoreRect,
    ): PartyCore {
        particlesCreated++
        with(party) {
            val randomSize = size[random.nextInt(size.size)]
            return PartyCore(
                location = position.get(drawArea).run { Vector(x, y) },
                width = randomSize.sizeInDp * pixelDensity,
                mass = randomSize.massWithVariance(),
                shape = getRandomShape(shapes),
                color = colors[random.nextInt(colors.size)],
                lifespan = timeToLive,
                fadeOut = fadeOutEnabled,
                fadeOutDuration = fadeOutDuration,
                velocity = getVelocity(),
                damping = party.damping,
                rotationSpeed2D = rotation.rotationSpeed() * party.rotation.multiplier2D,
                rotationSpeed3D = rotation.rotationSpeed() * party.rotation.multiplier3D,
                pixelDensity = pixelDensity,
            )
        }
    }


    private fun Rotation.rotationSpeed(): Float {
        if (!enabled) return 0f
        val randomValue = random.nextFloat() * 2f - 1f
        return speed + (speed * variance * randomValue)
    }

    private fun Party.getSpeed(): Float =
        if (maxSpeed == -1f) {
            speed
        } else {
            ((maxSpeed - speed) * random.nextFloat()) + speed
        }


    private fun Size.massWithVariance(): Float = mass + (mass * (random.nextFloat() * massVariance))


    private fun Party.getVelocity(): Vector {
        val speed = getSpeed()
        val radian = toRadians(getAngle())
        val vx = speed * cos(radian).toFloat()
        val vy = speed * sin(radian).toFloat()
        return Vector(vx, vy)
    }

    private fun Party.getAngle(): Double {
        if (spread == 0) return angle.toDouble()

        val minAngle = angle - (spread / 2)
        val maxAngle = angle + (spread / 2)
        return (maxAngle - minAngle) * random.nextDouble() + minAngle
    }

    private fun Position.get(drawArea: CoreRect): Position.Absolute {
        return when (this) {
            is Position.Absolute -> Position.Absolute(x, y)
            is Position.Relative -> {
                Position.Absolute(
                    drawArea.width * x.toFloat(),
                    drawArea.height * y.toFloat(),
                )
            }

            is Position.Between -> {
                val minPos = min.get(drawArea)
                val maxPos = max.get(drawArea)
                return Position.Absolute(
                    x = random.nextFloat().times(maxPos.x.minus(minPos.x)) + minPos.x,
                    y = random.nextFloat().times(maxPos.y.minus(minPos.y)) + minPos.y,
                )
            }
        }
    }


    private fun getRandomShape(shapes: List<Shape>): Shape {
        return shapes[random.nextInt(shapes.size)]
    }

    private fun isTimeElapsed(): Boolean {
        return when (emitterConfig.emittingTime) {
            0L -> false
            else -> elapsedTime >= emitterConfig.emittingTime
        }
    }

    override fun isFinished(): Boolean {
        return if (emitterConfig.emittingTime > 0L) {
            elapsedTime >= emitterConfig.emittingTime
        } else {
            false
        }
    }

    private fun toRadians(deg: Double): Double = deg / 180.0 * PI
}