package com.bitbytestudio.partyeffect.efeects

import com.bitbytestudio.partyeffect.models.Angle
import com.bitbytestudio.partyeffect.models.EffectPreset
import com.bitbytestudio.partyeffect.models.Emitter
import com.bitbytestudio.partyeffect.models.Party
import com.bitbytestudio.partyeffect.models.Position
import com.bitbytestudio.partyeffect.models.Spread
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

public class Effects {
    companion object {
        fun explode(): List<Party> {
            return listOf(
                Party(
                    speed = 0f,
                    maxSpeed = 30f,
                    damping = 0.9f,
                    spread = 360,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    emitter = Emitter(duration = 100.milliseconds).max(100),
                    // position = Position.Relative(0.5, 0.3)
                )
            )
        }

        fun parade(): List<Party> {
            val party = Party(
                speed = 10f,
                maxSpeed = 30f,
                damping = 0.9f,
                angle = Angle.RIGHT - 45,
                spread = Spread.SMALL,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 5.seconds).perSecond(30),
                position = Position.Relative(0.0, 0.5)
            )

            return listOf(
                party,
                party.copy(
                    angle = party.angle - 90, // flip angle from right to left
                    position = Position.Relative(1.0, 0.5)
                ),
            )
        }

        fun rain(): List<Party> {
            return listOf(
                Party(
                    speed = 0f,
                    maxSpeed = 15f,
                    damping = 0.9f,
                    angle = Angle.BOTTOM,
                    spread = Spread.ROUND,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    emitter = Emitter(duration = 5.seconds).perSecond(100),
                    position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
                )
            )
        }

        fun burst(): List<Party> {
            return listOf(
                Party(
                    speed = 20f,
                    maxSpeed = 40f,
                    damping = 0.8f,
                    angle = Angle.TOP,
                    spread = Spread.ROUND,
                    colors = listOf(0xffc107, 0xff5722, 0x03a9f4, 0x8bc34a),
                    emitter = Emitter(duration = 300.milliseconds).max(200),
                    position = Position.Relative(0.5, 0.5)
                )
            )
        }

        fun fountain(): List<Party> {
            return listOf(
                Party(
                    speed = 15f,
                    maxSpeed = 25f,
                    damping = 0.85f,
                    angle = Angle.TOP,
                    spread = Spread.WIDE,
                    colors = listOf(0xe91e63, 0x9c27b0, 0x3f51b5, 0x00bcd4),
                    emitter = Emitter(duration = 2.seconds).perSecond(50),
                    position = Position.Relative(0.5, 1.0)
                )
            )
        }

        fun wave(): List<Party> {
            val left = Party(
                speed = 12f,
                maxSpeed = 20f,
                damping = 0.88f,
                angle = Angle.RIGHT - 30,
                spread = Spread.SMALL,
                colors = listOf(0xff9800, 0x4caf50, 0x2196f3),
                emitter = Emitter(duration = 3.seconds).perSecond(20),
                position = Position.Relative(0.0, 0.7)
            )

            val right = left.copy(
                angle = Angle.LEFT + 30,
                position = Position.Relative(1.0, 0.7)
            )

            return listOf(left, right)
        }


        fun curtain(): List<Party> {
            return listOf(
                Party(
                    speed = 0f,
                    maxSpeed = 10f,
                    damping = 0.95f,
                    angle = Angle.BOTTOM,
                    spread = Spread.ROUND,
                    colors = listOf(0xffffff, 0xcccccc, 0x999999),
                    emitter = Emitter(duration = 6.seconds).perSecond(150),
                    position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
                )
            )
        }


//        val all = listOf(
//            explode(),
//            parade(),
//            rain(),
//            burst(),
//            fountain(),
//            wave(),
//            curtain(),
//        )


        val all = listOf(
            EffectPreset("Explode", explode()),
            EffectPreset("Parade", parade()),
            EffectPreset("Rain", rain()),
            EffectPreset("Burst", burst()),
            EffectPreset("Fountain", fountain()),
            EffectPreset("Wave", wave()),
            EffectPreset("Curtain", curtain())
        )
    }
}