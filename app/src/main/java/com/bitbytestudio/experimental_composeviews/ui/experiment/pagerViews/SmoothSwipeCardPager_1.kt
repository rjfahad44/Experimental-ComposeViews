package com.bitbytestudio.experimental_composeviews.ui.experiment.pagerViews

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.bitbytestudio.experimental_composeviews.utils.colorFromIndex
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> SmoothSwipeCardPager_1(
    items: List<T>,
    modifier: Modifier = Modifier,
    cardHeight: Dp = 220.dp,
    pagerHeight: Dp = 250.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 48.dp),
    pageSpacing: Dp = 16.dp,
    animationSpec: SpringSpec<Float> = spring(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioNoBouncy
    ),
    cardContent: @Composable (item: T, index: Int, bgColor: Color) -> Unit
) {
    val startIndex = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        initialPage = startIndex,
        initialPageOffsetFraction = 0f,
        pageCount = { Int.MAX_VALUE }
    )

    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(items.size),
        snapAnimationSpec = animationSpec
    )

    HorizontalPager(
        state = pagerState,
        flingBehavior = flingBehavior,
        contentPadding = contentPadding,
        pageSpacing = pageSpacing,
        modifier = modifier
            .fillMaxWidth()
            .height(pagerHeight)
    ) { page ->

        val actualPage = page % items.size
        val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

        val scale = lerp(0.95f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
        val alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))

        val bgColor = actualPage.colorFromIndex()

        Card(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .fillMaxWidth()
                .height(cardHeight),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            cardContent(items[actualPage], actualPage, bgColor)
        }
    }
}

