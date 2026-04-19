package com.bitbytestudio.experimental_composeviews.utils

enum class DemoPage(val title: String) {
    NO_GRAVITY_BOX("No Gravity Box"),
    CURVED_SLIDER("Curved Slider"),
    SHIMMER("Shimmer Effect"),
    MOVEABLE_BEE("Moveable Bee"),
    RANDOM_SHAKE("Random Shake"),
    RANDOM_MOVEABLE("Random Moveable"),
    DJ_PER_CHAR("Dj Lighting Per Character"),
    DJ_SHOW("DJ Show Light Effect"),
    SIMPLE_PAGER("Simple Pager"),
    STACK_PAGER("Fully Customizable Stack Pager"),
    PARTY_EFFECT("Party Effect"),

    LIQUID_GLASS_EFFECT("Liquid Glass Effect");

    companion object {
        val pages = listOf(
            NO_GRAVITY_BOX,
            CURVED_SLIDER,
            SHIMMER,
            MOVEABLE_BEE,
            RANDOM_SHAKE,
            RANDOM_MOVEABLE,
            DJ_PER_CHAR,
            DJ_SHOW,
            SIMPLE_PAGER,
            STACK_PAGER,
            PARTY_EFFECT,
            LIQUID_GLASS_EFFECT,
        )
        fun fromIndex(index: Int): DemoPage {
            val values = entries
            return values[index % values.size]
        }
    }
}
