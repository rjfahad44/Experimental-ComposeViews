package com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bitbytestudio.experimental_composeviews.ui.experiment.partyEffect.efeects.Effects

@Composable
fun PartyView() {
    var isAnimating by remember { mutableStateOf(false) }
    var index by remember { mutableIntStateOf(0) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        val (effectsName, parties) = Effects.all[index]
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                enabled = !isAnimating,
                onClick = { isAnimating = !isAnimating },
            ) {
                Text("Party Time (Prebuild Effects: ${effectsName}) 🥳")
            }
        }

        if (isAnimating) {
            PartyCanvas(
                modifier = Modifier.fillMaxSize(),
                parties = parties,
                onParticleSystemEnded = { _, activeSystems ->
                    if (activeSystems == 0 && isAnimating) {
                        isAnimating = false
                        index = (index + 1) % Effects.all.size
                    }
                },
            )
        }
    }
}








