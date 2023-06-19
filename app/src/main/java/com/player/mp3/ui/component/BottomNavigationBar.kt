package com.player.mp3.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.player.mp3.R

@Composable
fun NavigationBar(
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        IconButton(onClick = onHomeClick) {
            Icon(
                painter = painterResource(id = R.drawable.tasty_music_logo),
                contentDescription = ""
            )
        }
        IconButton(onClick = onFavoritesClick) {
            Icon(
                painter = painterResource(id = R.drawable.heart_solid),
                contentDescription = ""
            )
        }
        IconButton(onClick = onSettingsClick) {
            Icon(
                painter = painterResource(id = R.drawable.up_right_from_square_solid),
                contentDescription =""
            )
        }
    }
}