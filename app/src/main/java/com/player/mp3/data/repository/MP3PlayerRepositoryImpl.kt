package com.player.mp3.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.player.mp3.domain.model.AudioMetadata
import com.player.mp3.domain.repository.MP3PlayerRepository
import com.player.mp3.util.audio.MetadataHelper
import com.player.mp3.util.audio.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MP3PlayerRepositoryImpl @Inject constructor(
    private val metadataHelper: MetadataHelper,
    private val userPreferences: UserPreferences
):  MP3PlayerRepository {
    override suspend fun getAudioList(): List<AudioMetadata> {
        return withContext(Dispatchers.IO){
            metadataHelper.getAudioList()
        }
    }

    override suspend fun loadCoverBitmap(context: Context, uri: Uri): Bitmap? {
        return withContext(Dispatchers.IO){
            metadataHelper.getAlbumArt(
                context = context,
                uri = uri
            )
        }
    }

    override suspend fun likeOrNotSong(id: Long) {
        withContext(Dispatchers.IO) {
            userPreferences.likeOrNotSong(id = id)
        }
    }

    override fun getFavoriteSongs(): Flow<List<Long>> {
        return userPreferences.favoriteSongs
    }


}