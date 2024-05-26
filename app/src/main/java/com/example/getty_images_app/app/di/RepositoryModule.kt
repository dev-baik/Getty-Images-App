package com.example.getty_images_app.app.di

import com.example.getty_images_app.data.repository.MainRepositoryImpl
import com.example.getty_images_app.domain.repository.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMainRepository(repository: MainRepositoryImpl): MainRepository
}