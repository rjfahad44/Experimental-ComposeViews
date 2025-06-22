package com.bitbytestudio.partyeffect.models

import com.bitbytestudio.partyeffect.interfaces.CoreRect

public sealed interface Shape {
    public data object Circle : Shape {
        // Default replacement for RectF
        internal val rect: CoreRect = CoreRect.CoreRectImpl()
    }

    public data object Square : Shape

    public data class Rectangle(
        val heightRatio: Float,
    ) : Shape {
        init {
            require(heightRatio in 0f..1f)
        }
    }
}