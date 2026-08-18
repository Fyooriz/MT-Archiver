package dev.mtarchiver.core.ai.di

import dev.mtarchiver.core.ai.api.AIService
import dev.mtarchiver.core.ai.impl.AIServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AIModule {
    @Binds
    @Singleton
    abstract fun bindAIService(impl: AIServiceImpl): AIService
}
