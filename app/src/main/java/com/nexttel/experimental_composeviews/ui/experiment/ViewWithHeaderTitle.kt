package com.nexttel.experimental_composeviews.ui.experiment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.nio.file.WatchEvent

@Composable
fun ViewWithHeaderTitle(
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color = Color.Black,
    titleFontSize: TextUnit = 18.sp,
    titleFontWeight: FontWeight = FontWeight.SemiBold,
    content: @Composable BoxScope.() -> Unit
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = titleColor,
            fontSize = titleFontSize,
            fontWeight = titleFontWeight,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Box(modifier = Modifier.fillMaxSize()){
            content()
        }
    }
}