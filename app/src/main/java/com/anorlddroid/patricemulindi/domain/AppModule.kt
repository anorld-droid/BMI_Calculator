package com.anorlddroid.patricemulindi.domain

import com.anorlddroid.patricemulindi.repository.*
import com.anorlddroid.patricemulindi.repository.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@InstallIn(ViewModelComponent::class)
@Module
abstract class AppModule {
    @Binds
    abstract fun provideBMIRepository(mBMIRepo: BMIRepositoryImpl): BMIRepository

    @Binds
    abstract fun provideCalculateBMIUseCase(mBMIUseCase: CalculateBMIUseCaseImpl): CalculateBMIUseCase

    @Binds
    abstract fun provideOPenPlayMarketUseCase(mOPenPlayMarketUseCase: OPenPlayMarketUseCaseImpl): OPenPlayMarketUseCase

    @Binds
    abstract fun provideFileRepository(mFileRepo: FileRepositoryImpl): FileRepository

    @Binds
    abstract fun provideScreenshotRepository(mScreenshotRepo: ScreenshotRepositoryImpl): ScreenshotRepository

    @Binds
    abstract fun provideShareScreenshotUseCase(mShareScreenshotUseCase: ShareScreenshotUseCaseImpl): ShareScreenshotUseCase
}