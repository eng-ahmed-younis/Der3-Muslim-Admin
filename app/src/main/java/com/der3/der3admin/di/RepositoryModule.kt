package com.der3.der3admin.di

import com.der3.der3admin.data.repo.BroadcastRepositoryImpl
import com.der3.der3admin.data.repo.StorageRepositoryImpl
import com.der3.der3admin.data.repo.TokenRepositoryImpl
import com.der3.der3admin.domain.repo.BroadcastRepository
import com.der3.der3admin.domain.repo.StorageRepository
import com.der3.der3admin.domain.repo.TokenRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module for providing Repository implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides the singleton instance of [BroadcastRepository].
     */
    @Provides
    @Singleton
    fun provideBroadcastRepository(
        broadcastRepositoryImpl: BroadcastRepositoryImpl
    ): BroadcastRepository {
        return broadcastRepositoryImpl
    }

    /**
     * Provides the singleton instance of [TokenRepository].
     */
    @Provides
    @Singleton
    fun provideTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository {
        return tokenRepositoryImpl
    }

    /**
     * Provides the singleton instance of [StorageRepository].
     */
    @Provides
    @Singleton
    fun provideStorageRepository(
        storageRepositoryImpl: StorageRepositoryImpl
    ): StorageRepository {
        return storageRepositoryImpl
    }

    /**
     * Provides the singleton instance of [FirebaseDatabase].
     */
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    /**
     * Provides the singleton instance of [FirebaseStorage].
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}
