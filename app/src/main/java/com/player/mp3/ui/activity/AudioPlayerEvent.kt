package com.player.mp3.ui.activity

import android.content.Context
import com.player.mp3.domain.model.AudioMetadata

sealed class AudioPlayerEvent {
    data class InitAudio(
        val audio: AudioMetadata,
        val context: Context,
        val onAudioInitialized: () -> Unit
    ): AudioPlayerEvent()


    data class Seek(
        val position: Float
    ): AudioPlayerEvent()

    data class LikeOrNotSong(val id:Long): AudioPlayerEvent()
    object Play: AudioPlayerEvent()
    object Pause: AudioPlayerEvent()
    object Stop: AudioPlayerEvent()
    object LoadMediaList: AudioPlayerEvent()
    object HideLoadingDialog: AudioPlayerEvent()



}