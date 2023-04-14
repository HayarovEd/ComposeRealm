package com.eedurda77.composerealm.di

import com.eedurda77.composerealm.data.remote.CarsApi
import com.eedurda77.composerealm.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.RealmConfiguration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Provides
    @Singleton
    fun provideApi(): CarsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CarsApi::class.java)
    }

    private val realmVersion = 1L

    @Provides
    @Singleton
    fun providesRealmConfig(): RealmConfiguration {
        return RealmConfiguration.Builder()
            .schemaVersion(realmVersion)
            .build()
    }

}