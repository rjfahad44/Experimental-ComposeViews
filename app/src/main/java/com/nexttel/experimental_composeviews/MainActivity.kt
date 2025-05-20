package com.nexttel.experimental_composeviews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.nexttel.experimental_composeviews.ui.experiment.ExperimentalViews
import com.nexttel.experimental_composeviews.ui.theme.ExperimentalComposeViewsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExperimentalComposeViewsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExperimentalViews(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}