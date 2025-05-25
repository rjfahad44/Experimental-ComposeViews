package com.bitbytestudio.experimental_composeviews.utils

enum class DemoPage(val title: String) {
    SHIMMER("Shimmer Effect"),
    MOVEABLE_BEE("Moveable Bee"),
    RANDOM_SHAKE("Random Shake"),
    RANDOM_MOVEABLE("Random Moveable"),
    DJ_PER_CHAR("Dj Lighting Per Character"),
    DJ_SHOW("DJ Show Light Effect"),
    SIMPLE_PAGER("Simple Pager"),
    STACK_PAGER("Fully Customizable Stack Pager");

    companion object {
        val pages = listOf(
            SHIMMER,
            MOVEABLE_BEE,
            RANDOM_SHAKE,
            RANDOM_MOVEABLE,
            DJ_PER_CHAR,
            DJ_SHOW,
            SIMPLE_PAGER,
            STACK_PAGER,
        )
        fun fromIndex(index: Int): DemoPage {
            val values = entries
            return values[index % values.size]
        }
    }
}
