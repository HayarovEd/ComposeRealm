package com.eedurda77.composerealm.di

import com.eedurda77.composerealm.data.repository.RepositoryImpl
import com.eedurda77.composerealm.domain.repository.Repo
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
    abstract fun bindRepository(
        repositoryImpl: RepositoryImpl
    ): Repo
}