package com.bitbytestudio.experimental_composeviews.ui.experiment.pagerViews


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt


@Composable
internal inline fun <reified T> StackSwipeCardPager(
    items: List<T>,
    modifier: Modifier = Modifier,
    visibleCards: Int = 3,
    cardHeight: Dp = 220.dp,
    cardWidth: Dp = 300.dp,
    cardSpacing: Dp = 16.dp,
    swipeThreshold: Float = 150f,
    infinityLoop: Boolean = true,
    applyFIFO: Boolean = true,
    stackFromTop: Boolean = false,
    crossinline cardContent: @Composable (item: T, index: Int, bgColor: Color) -> Unit,
) {
    // States & Animations
    val cardList = remember { mutableStateListOf(*items.toTypedArray()) }
    val cardOffset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val cardRotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var isDragging by remember { mutableStateOf(false) }

    // Random color per card (stable)
    val colorMap = remember(cardList) { cardList.associateWith { randomColor() }.toMutableMap() }

    val alpha by remember { derivedStateOf { 1f - (abs(cardOffset.value.x) / 600f).coerceIn(0f, 1f) } }

    val topCardKey = cardList.firstOrNull()
    val fadeInAlpha by animateFloatAsState(
        targetValue = if (topCardKey != null) 1f else 0f,
        animationSpec = tween(250),
        label = "fadeInAlpha"
    )
    val scaleIn by animateFloatAsState(
        targetValue = if (topCardKey != null) 1f else 0.95f,
        animationSpec = tween(250),
        label = "scaleIn"
    )

    // Visual Card Stack
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val visibleCardStack = remember(topCardKey, applyFIFO) {
            val sliced = if (applyFIFO) cardList.take(visibleCards) else cardList.takeLast(visibleCards)
            if (applyFIFO) sliced.reversed() else sliced
        }.also {
            if (stackFromTop) it else it.reversed()
        }

        visibleCardStack.forEachIndexed { index, item ->
            val cardIndex = visibleCardStack.lastIndex - index
            val isTop = cardIndex == 0
            val baseScale = 1f - (0.04f * cardIndex)
            val offsetY = if (stackFromTop) -(cardSpacing * cardIndex) else cardSpacing * cardIndex

            CardBox(
                modifier = Modifier
                    .zIndex(if (isTop) 1f else 0f)
                    .offset {
                        val x = if (isTop) cardOffset.value.x.roundToInt() else 0
                        val y = if (isTop) cardOffset.value.y.roundToInt() else offsetY.roundToPx()
                        IntOffset(x, y)
                    }
                    .graphicsLayer {
                        scaleX = if (isTop) scaleIn * baseScale else baseScale
                        scaleY = if (isTop) scaleIn * baseScale else baseScale
                        rotationZ = if (isTop) cardRotation.value else 0f
                        this.alpha = if (isTop) alpha * fadeInAlpha else 1f
                    }
                    .pointerInput(isTop) {
                        if (!isTop) return@pointerInput

                        detectDragGestures(
                            onDragStart = { isDragging = true },
                            onDragEnd = {
                                isDragging = false
                                handleCardSwipe(
                                    cardList = cardList,
                                    applyFIFO = applyFIFO,
                                    infinityLoop = infinityLoop,
                                    swipeThreshold = swipeThreshold,
                                    cardOffset = cardOffset,
                                    cardRotation = cardRotation,
                                    scope = scope
                                )
                            },
                            onDrag = { _, dragAmount ->
                                if (isDragging) {
                                    scope.launch {
                                        val newOffset = cardOffset.value + dragAmount
                                        cardOffset.snapTo(newOffset)
                                        cardRotation.snapTo(newOffset.x / 20f)
                                    }
                                }
                            }
                        )
                    },
                cardWidth = cardWidth,
                cardHeight = cardHeight,
            ) {
                cardContent(item, index, colorMap[item] ?: Color.Gray)
            }
        }
    }
}

@Composable
private fun CardBox(
    modifier: Modifier,
    cardWidth: Dp,
    cardHeight: Dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .size(cardWidth, cardHeight)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

private fun <T> handleCardSwipe(
    cardList: SnapshotStateList<T>,
    applyFIFO: Boolean,
    infinityLoop: Boolean,
    swipeThreshold: Float,
    cardOffset: Animatable<Offset, AnimationVector2D>,
    cardRotation: Animatable<Float, AnimationVector1D>,
    scope: CoroutineScope,
) {
    val offsetX = cardOffset.value.x
    scope.launch {
        if (abs(offsetX) > swipeThreshold) {
            // Animate out
            cardOffset.animateTo(Offset(offsetX * 3, cardOffset.value.y), tween(250))

            val removed = if (applyFIFO) cardList.removeFirstOrNull() else cardList.removeLastOrNull()
            removed?.let {
                if (infinityLoop) {
                    if (applyFIFO) cardList.add(it) else cardList.add(0, it)
                }
            }
            // Reset
            cardOffset.snapTo(Offset.Zero)
            cardRotation.snapTo(0f)
        } else {
            // Revert
            cardOffset.animateTo(Offset.Zero, tween(250))
            cardRotation.animateTo(0f)
        }
    }
}

fun randomColor(): Color {
    val colors = listOf(
        Color(0xFF2196F3),
        Color(0xFFFF9800),
        Color(0xFF4CAF50),
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFFFF5722),
    )
    return colors.random()
}

