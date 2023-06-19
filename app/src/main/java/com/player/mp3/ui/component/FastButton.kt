package com.player.mp3.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.player.mp3.ui.theme.Gray400
import com.player.mp3.ui.theme.onSurface

@Composable
fun FastButton(
    enabled: Boolean,
    onClick: () -> Unit,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier
){
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "",
            modifier = Modifier.padding(all = 8.dp),
            tint = if(enabled) onSurface else Gray400)


        
    }
}