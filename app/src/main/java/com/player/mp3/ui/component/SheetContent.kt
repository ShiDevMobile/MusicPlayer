package com.player.mp3.ui.component

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.player.mp3.R
import com.player.mp3.ui.activity.AudioPlayerEvent
import com.player.mp3.ui.activity.AudioPlayerState
import com.player.mp3.ui.activity.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetContent(
    state: AudioPlayerState,
    scope: CoroutineScope,
    mainViewModel: MainViewModel,
    sheetState: ModalBottomSheetState,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(top = 16.dp)
    ) {
        if (state.audioList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                WarningMessage(
                    text = stringResource(id = R.string.txt_no_media),
                    iconResId = R.drawable.circle_info_solid,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.lbl_tracks),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(top = 12.dp, bottom = 3.dp),
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colors.onBackground
                )
            }
            state.audioList.forEach { audio ->
                Track(
                    audio = audio,
                    isPlaying = audio.songId == state.selectedAudio.songId,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                        .requiredHeight(height = 100.dp),
                    onClick = {
                        scope.launch {
                            mainViewModel.onEvent(event = AudioPlayerEvent.Stop)
                            sheetState.hide()
                            mainViewModel.onEvent(event = AudioPlayerEvent.InitAudio(
                                audio = it,
                                context = context,
                                onAudioInitialized = {
                                    mainViewModel.onEvent(event = AudioPlayerEvent.Play)
                                }
                            ))
                        }
                    }
                )
                Divider(modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}