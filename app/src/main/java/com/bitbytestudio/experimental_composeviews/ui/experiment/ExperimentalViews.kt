package com.bitbytestudio.experimental_composeviews.ui.experiment


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitbytestudio.experimental_composeviews.R
import com.bitbytestudio.experimental_composeviews.ui.experiment.djLightEffect.DJLightShowBox
import com.bitbytestudio.experimental_composeviews.ui.experiment.djTextEffect.DJLightingPerCharacterText
import com.bitbytestudio.experimental_composeviews.ui.experiment.fullScreenRandomMoverBox.FullScreenRandomMoverBox
import com.bitbytestudio.experimental_composeviews.ui.experiment.pagerViews.SmoothSwipeCardPager_1
import com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.PartyView
import com.bitbytestudio.experimental_composeviews.ui.experiment.shakableView.RandomShakeBox
import com.bitbytestudio.experimental_composeviews.ui.experiment.shimmerEffect.shimmerEffect
import com.bitbytestudio.experimental_composeviews.utils.DemoPage
import com.bitbytestudio.movableimage.MoveableImage
import com.bitbytestudio.stackswipecardpager.StackSwipeCardPager


@Composable
fun ExperimentalViews(
    modifier: Modifier = Modifier,
) {
    val beePainter = painterResource(id = R.drawable.bee)
    val cards = remember { listOf("One", "Two", "Three", "Four", "Five") }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { Int.MAX_VALUE /*pages.size*/ }
    )

    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        snapAnimationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioNoBouncy
        ),
    )

    PartyView()

//    VerticalPager(
//        state = pagerState,
//        flingBehavior = flingBehavior,
//        contentPadding = PaddingValues(0.dp),
//        beyondViewportPageCount = 0,
//        modifier = modifier
//            .fillMaxSize()
//    ) { pageIndex ->
//
//        val page = DemoPage.fromIndex(pageIndex)
//        ViewWithHeaderTitle(
//            title = page.title,
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            when (page) {
//                DemoPage.MOVEABLE_BEE ->{
//                    MoveableImage(
//                        beeCount = 5,
//                        beeSize = 35.dp,
//                        beeSpeedMillis = 1500,
//                        easing = LinearEasing,
//                        beePainter = beePainter
//                    ) {
//                        // add any composable as you want
//                    }
//                }
//
//                DemoPage.SHIMMER -> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .shimmerEffect(
//                                shimmerWidthDp = LocalConfiguration.current.screenWidthDp.dp
//                            ),
//                    )
//                }
//
//                DemoPage.RANDOM_SHAKE -> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        RandomShakeBox(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            shakeRange = 30,
//                            durationMillis = 1000
//                        ) {
//                            Text(text = "Shakable view")
//                        }
//                    }
//                }
//
//                DemoPage.RANDOM_MOVEABLE -> {
//                    FullScreenRandomMoverBox(
//                        modifier = Modifier
//                            .fillMaxSize(),
//                        durationMillis = 1000
//                    ) {
//                        Text(text = "Random Moveable")
//                    }
//                }
//
//                DemoPage.DJ_PER_CHAR -> {
//                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                        DJLightingPerCharacterText(
//                            modifier = Modifier.fillMaxWidth().padding(32.dp),
//                            text = "Dj Lighting Effect Per Character Demo",
//                            fontSize = 32.sp
//                        )
//                    }
//                }
//
//                DemoPage.DJ_SHOW -> {
//                    val beamColors = listOf(
//                        Color.Red to Color.Yellow,
//                        Color.Cyan to Color.Blue,
//                        Color.Magenta to Color.Green
//                    )
//                    DJLightShowBox(
//                        lightCount = 5,
//                        beamColors = beamColors,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(Color.White)
//                    ) {
//                        // add any composable as you want
//                    }
//                }
//
//                DemoPage.SIMPLE_PAGER -> {
//                    SmoothSwipeCardPager_1(
//                        items = cards
//                    ) { card, index, bgColor ->
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(bgColor),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = card,
//                                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
//                            )
//                        }
//                    }
//                }
//
//                DemoPage.STACK_PAGER -> {
//                    StackSwipeCardPager(
//                        modifier = Modifier.fillMaxSize(),
//                        items = cards,
//                        visibleCards = 3,
//                        cardSpacing = 20.dp,
//                        cardWidth = (LocalConfiguration.current.screenWidthDp.dp * 0.9f),
//                        infinityLoop = true,
//                        applyFIFO = true,
//                        stackFromTop = false
//                    ) { item, index, bgColor ->
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(
//                                    color = bgColor,
//                                    shape = RoundedCornerShape(20.dp)
//                                ),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = item,
//                                style = MaterialTheme.typography.headlineMedium,
//                                color = Color.White
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
}