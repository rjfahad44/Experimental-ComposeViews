package com.nexttel.experimental_composeviews.ui.experiment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexttel.experimental_composeviews.ui.experiment.djLightEffectView.DjLightStageEffect
import com.nexttel.experimental_composeviews.ui.experiment.djTextEffect.DJLightingPerCharacterText
import com.nexttel.experimental_composeviews.ui.experiment.shakableView.RandomShakeBox
import com.nexttel.experimental_composeviews.ui.experiment.shimmer.shimmerEffect

@Composable
fun ExperimentalViews(
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            ViewWithHeaderTitle(
                title = "Shimmer Effect",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ){
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
            ){
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
            ){
                DJLightingPerCharacterText(
                    text = "Dj Lighting Effect Per Character Demo",
                    fontSize = 18.sp
                )
            }
        }

        item {
            ViewWithHeaderTitle(
                title = "Dj Corner Light Effect",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ){
                DjLightStageEffect(
                    modifier = Modifier.fillMaxSize(),
                    beamCount = 2,
                    rotationRange = 20f,
                    rotationSpeed = 12f
                ) {
                    Text(
                        text = "🎧 Let's Party!",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Black,
                        fontSize = 28.sp
                    )
                }
            }
        }
    }
}