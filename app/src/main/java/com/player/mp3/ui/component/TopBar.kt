
package com.player.mp3.ui.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp



@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit,
    title: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {


    Box(modifier = modifier.fillMaxWidth()) {

        Box(modifier = Modifier.align(alignment = Alignment.CenterStart)) {
            leadingIcon()
        }

        Box(modifier = Modifier.align(alignment = Alignment.Center).padding(horizontal = 40.dp)) {
            title?.invoke()
        }

        Box(modifier = Modifier.align(alignment = Alignment.CenterEnd)) {
            if (trailingIcon == null) {
                Spacer(modifier = Modifier.requiredWidth(width = 26.dp))
            } else {
                trailingIcon()
            }
        }

    }

}