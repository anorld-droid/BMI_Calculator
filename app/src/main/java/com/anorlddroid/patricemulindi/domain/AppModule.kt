package com.anorlddroid.patricemulindi.domain

import com.anorlddroid.patricemulindi.usecases.BMIRepository
import com.anorlddroid.patricemulindi.usecases.BMIRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
abstract class AppModule {
    @Binds
    abstract fun provideBMIRepository(repo: BMIRepositoryImpl):BMIRepository
}