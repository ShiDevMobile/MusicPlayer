package com.player.mp3.ui.screen

import android.Manifest
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.player.mp3.R
import com.player.mp3.ui.activity.AudioPlayerEvent
import com.player.mp3.ui.activity.AudioPlayerState
import com.player.mp3.ui.activity.MainViewModel
import com.player.mp3.ui.component.FastButton
import com.player.mp3.ui.component.LikeButton
import com.player.mp3.ui.component.PlayPauseButton
import com.player.mp3.ui.component.StackedBarVisualizer
import com.player.mp3.ui.component.TimeBar
import com.player.mp3.ui.component.TopBar
import com.player.mp3.ui.theme.Black3
import com.player.mp3.util.FORWARD_BACKWARD_STEP
import com.player.mp3.util.isNotEmpty
import com.player.mp3.util.setupPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    statusBarHeight: Dp,
    navigationBarHeight: Dp,
    state: AudioPlayerState,
    mainViewModel: MainViewModel,
    context: Context,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    scope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    screenHeight: Dp
) {
    val rotationState = rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0f,
        targetValue = if (state.isPlaying) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    Surface(
        modifier = Modifier
            .fillMaxSize()
            //.padding(top = statusBarHeight, bottom = navigationBarHeight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .background(color = MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopBar(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .requiredHeight(height = 80.dp),
                leadingIcon = {
                    LikeButton(
                        isFavorite = state.favoriteSongs.contains(state.selectedAudio.songId),
                        enabled = state.selectedAudio.isNotEmpty(),
                        onClick = {
                            mainViewModel.onEvent(
                                event = AudioPlayerEvent.LikeOrNotSong(
                                    id = state.selectedAudio.songId
                                )
                            )
                        }
                    )
                },
                title = {
                    if (state.selectedAudio.isNotEmpty()) {
                        val artist = if (state.selectedAudio.artist.contains(
                                "<unknown>",
                                ignoreCase = true
                            )
                        )
                            "" else "${state.selectedAudio.artist} - "
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(artist)
                                }
                                append(state.selectedAudio.songTitle)
                            },
                            color = MaterialTheme.colors.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.h6,
                            )
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            setupPermissions(
                                context = context,
                                permissions = arrayOf(
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ),
                                launcher = launcher,
                                onPermissionGranted = {
                                    scope.launch {
                                        if (state.audioList.isEmpty()) {
                                            mainViewModel.onEvent(event = AudioPlayerEvent.LoadMediaList)
                                        }
                                        sheetState.show()
                                    }

                                }
                            )
                        }
                    )
                    {
                        Icon(
                            painter = painterResource(R.drawable.ellipsis_vertical_outline),
                            contentDescription = "",
                            modifier = Modifier.requiredSize(size = 26.dp),
                            tint = MaterialTheme.colors.onSurface
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.requiredHeight(height = 16.dp))


            state.selectedAudio.cover?.let { cover ->
                    Surface(
                        modifier =Modifier.rotate(rotationState.value)
                            .requiredHeight(height = screenHeight * 0.4f),
                        shape = CircleShape,
                        elevation = 8.dp,
                        color = MaterialTheme.colors.onSurface
                    ) {
                        Image(
                            bitmap = cover.asImageBitmap(),
                            contentScale = ContentScale.Crop,
                            contentDescription = "",
                            )
                    }

            } ?: Box(
                modifier = Modifier.requiredHeight(height = screenHeight * 0.4f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    elevation = 8.dp,
                    shape = CircleShape,
                    modifier = Modifier.fillMaxHeight(fraction = 0.5f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.musical_note_music_svgrepo_com),
                        contentScale = ContentScale.FillHeight,
                        contentDescription = "Default Picture",
                        modifier = Modifier.padding(
                            top = 25.dp,
                            bottom = 26.dp,
                            start = 8.dp,
                            end = 20.dp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.requiredHeight(height = 16.dp))

            StackedBarVisualizer(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(height = 200.dp)
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                shape = MaterialTheme.shapes.large,
                barCount = 32,
                barColors = listOf(
                    Color.Blue,
                    Color.Magenta
                ),
                stackedBarBackgroundColor = if (isSystemInDarkTheme()) Black3 else
                    MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
                data = mainViewModel.visualizerData.value
            )


            Spacer(modifier = Modifier.requiredHeight(height = 10.dp))

            TimeBar(
                currentPosition = state.currentPosition,
                onValueChange = { position ->
                    mainViewModel.onEvent(
                        event = AudioPlayerEvent.Seek(position = position)
                    )
                },
                duration = state.selectedAudio.duration,

                )

            Spacer(modifier = Modifier.requiredHeight(height = 10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            )
            {
                FastButton(
                    enabled = state.currentPosition > FORWARD_BACKWARD_STEP,
                    onClick = {
                        mainViewModel.onEvent(
                            event = AudioPlayerEvent.Seek(
                                position = state.currentPosition - FORWARD_BACKWARD_STEP
                            )
                        )

                    },
                    iconResId = R.drawable.backward_solid
                )
                PlayPauseButton(
                    enabled = state.selectedAudio.isNotEmpty(),
                    modifier = Modifier.padding(horizontal = 26.dp),
                    onPlay = {
                        mainViewModel.onEvent(event = AudioPlayerEvent.Play)
                    },
                    onPause = {
                        mainViewModel.onEvent(event = AudioPlayerEvent.Pause)
                    },
                    isPlaying = state.isPlaying
                )
                FastButton(
                    enabled = state.currentPosition < (state.selectedAudio.duration - FORWARD_BACKWARD_STEP),
                    onClick = {
                        mainViewModel.onEvent(
                            event = AudioPlayerEvent.Seek(
                                position = state.currentPosition + FORWARD_BACKWARD_STEP
                            )
                        )

                    },
                    iconResId = R.drawable.forward_solid
                )
            }



        }


    }
}