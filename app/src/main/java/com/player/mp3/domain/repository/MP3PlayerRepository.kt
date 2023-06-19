package com.player.mp3.domain.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.player.mp3.domain.model.AudioMetadata
import kotlinx.coroutines.flow.Flow

interface MP3PlayerRepository {


    suspend fun getAudioList(): List<AudioMetadata>

    suspend fun loadCoverBitmap(context: Context, uri: Uri): Bitmap?

    suspend fun likeOrNotSong(id: Long)
    fun getFavoriteSongs(): Flow<List<Long>>
}