package com.anorlddroid.patricemulindi.domain

import com.anorlddroid.patricemulindi.repository.BMIRepository
import com.anorlddroid.patricemulindi.repository.BMIRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@InstallIn(ViewModelComponent::class)
@Module
abstract class AppModule {
    @Binds
    abstract fun provideBMIRepository(repo: BMIRepositoryImpl):BMIRepository
}