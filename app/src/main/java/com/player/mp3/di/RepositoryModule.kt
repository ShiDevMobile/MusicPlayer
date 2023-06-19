package com.player.mp3.di

import com.player.mp3.data.repository.MP3PlayerRepositoryImpl
import com.player.mp3.domain.repository.MP3PlayerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMP3PlayerRepository(
        repository: MP3PlayerRepositoryImpl
    ): MP3PlayerRepository
}