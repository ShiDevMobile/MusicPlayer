package com.player.mp3.ui.component

import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.format.TextStyle

@Composable
fun MarqueeText(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        while (true) {
            scrollState.animateScrollTo(
                scrollState.maxValue,
                animationSpec = TweenSpec(durationMillis = 5000) // Thời gian chạy của title (5 giây trong ví dụ)
            )
            scrollState.animateScrollTo(
                scrollState.value,
                animationSpec = TweenSpec(durationMillis = 0)
            )
        }
    }
}
