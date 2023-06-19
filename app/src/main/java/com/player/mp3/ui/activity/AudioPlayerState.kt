package com.player.mp3.ui.activity

import com.player.mp3.domain.model.AudioMetadata

data class AudioPlayerState(
    val isLoading: Boolean = false,
    val audioList: List<AudioMetadata> = emptyList(),
    val selectedAudio: AudioMetadata = AudioMetadata.emptyMetadata(),
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val favoriteSongs: List<Long> = emptyList(),
    val isCoverRotating: Boolean = false,

)