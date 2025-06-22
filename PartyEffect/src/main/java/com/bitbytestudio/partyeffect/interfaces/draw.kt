package com.bitbytestudio.partyeffect.interfaces

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.bitbytestudio.partyeffect.models.Shape


internal fun Shape.draw(
    drawScope: DrawScope,
    particle: Particle,
) {
    when (this) {
        Shape.Circle -> {
            val offsetMiddle = particle.width / 2
            drawScope.drawCircle(
                color = Color(particle.color),
                center = Offset(particle.x + offsetMiddle, particle.y + offsetMiddle),
                radius = particle.width / 2,
            )
        }
        Shape.Square -> {
            drawScope.drawRect(
                color = Color(particle.color),
                topLeft = Offset(particle.x, particle.y),
                size = Size(particle.width, particle.height),
            )
        }
        is Shape.Rectangle -> {
            val size = particle.width
            val height = size * heightRatio
            drawScope.drawRect(
                color = Color(particle.color),
                topLeft = Offset(particle.x, particle.y),
                size = Size(size, height),
            )
        }
    }
}