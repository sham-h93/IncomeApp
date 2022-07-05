package com.app.incomeapp.di

import android.content.Context
import androidx.room.Room
import com.app.incomeapp.db.AppDatabase
import com.app.incomeapp.db.IncomeCostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "IncomeCostDb"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(appDb: AppDatabase): IncomeCostDao {
        return appDb.getDao()
    }

}