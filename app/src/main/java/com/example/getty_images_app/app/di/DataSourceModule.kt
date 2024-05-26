package com.example.getty_images_app.app.di

import com.example.getty_images_app.data.source.main.MainDataSource
import com.example.getty_images_app.data.source.main.MainDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindsMainDataSource(mainDataSourceImpl: MainDataSourceImpl): MainDataSource
}