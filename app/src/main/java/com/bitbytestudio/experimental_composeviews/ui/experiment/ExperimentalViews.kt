package com.bitbytestudio.experimental_composeviews.ui.experiment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.bitbytestudio.experimental_composeviews.ui.experiment.movableBee.MovableBee
import com.bitbytestudio.experimental_composeviews.ui.experiment.pagerViews.SmoothSwipeCardPager_1
import com.bitbytestudio.experimental_composeviews.ui.experiment.pagerViews.StackSwipeCardPager
import com.bitbytestudio.experimental_composeviews.ui.experiment.shakableView.RandomShakeBox
import com.bitbytestudio.experimental_composeviews.ui.experiment.shimmerEffect.shimmerEffect

@Composable
fun ExperimentalViews(
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val beePainter = painterResource(id = R.drawable.bee)
    val cards = listOf("One", "Two", "Three", "Four", "Five")

    MovableBee(
        beeCount = 5,
        beeSize = 25.dp,
        beeSpeedMillis = 1000,
        beePainter = beePainter
    ) {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .systemBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                ViewWithHeaderTitle(
                    title = "Shimmer Effect",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmerEffect(
                                durationMillis = 2500,
                                shimmerColors = listOf(
                                    Color.Red.copy(alpha = 0.3f),
                                    Color.Green.copy(alpha = 0.7f),
                                    Color.Blue.copy(alpha = 0.3f),
                                )
                            ),
                    )
                }
            }

            item {
                ViewWithHeaderTitle(
                    title = "Random Shake",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    RandomShakeBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        shakeRange = 15,
                        durationMillis = 300
                    ) {
                        Text(text = "Shakable view")
                    }
                }
            }

            item {
                ViewWithHeaderTitle(
                    title = "Dj Lighting Per Character",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    DJLightingPerCharacterText(
                        text = "Dj Lighting Effect Per Character Demo",
                        fontSize = 18.sp
                    )
                }
            }

            item {
                ViewWithHeaderTitle(
                    title = "DJ Show Light Effect",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    val beamColors = listOf(
                        Color.Red to Color.Yellow,
                        Color.Cyan to Color.Blue,
                        Color.Magenta to Color.Green
                    )
                    DJLightShowBox(
                        lightCount = 5,
                        beamColors = beamColors,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        // add any composable as you want
                    }
                }
            }

            item {
                ViewWithHeaderTitle(
                    title = "Pager: 1",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    SmoothSwipeCardPager_1(
                        items = cards
                    ) { card, index, bgColor ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = card,
                                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
                            )
                        }
                    }
                }
            }

            item {
                ViewWithHeaderTitle(
                    title = "Fully Customizable Stack Pager",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    StackSwipeCardPager(
                        modifier = Modifier.fillMaxSize(),
                        items = cards,
                        visibleCards = 3,
                        cardSpacing = 20.dp,
                        cardWidth = (LocalConfiguration.current.screenWidthDp.dp * 0.9f),
                        infinityLoop = true,
                        applyFIFO = true,
                        stackFromTop = false
                    ) { item, index, bgColor ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = bgColor,
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item, style = MaterialTheme.typography.headlineMedium, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}