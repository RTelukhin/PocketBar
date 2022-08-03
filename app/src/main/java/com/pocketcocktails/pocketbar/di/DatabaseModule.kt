package com.pocketcocktails.pocketbar.di

import android.content.Context
import androidx.room.Room
import com.pocketcocktails.pocketbar.data.room.FavoriteDatabase
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {

    @Provides
    fun provideDatabase(appContext: Context): FavoriteDatabase =
        Room
            .databaseBuilder(
                appContext,
                FavoriteDatabase::class.java,
                "favorites_database.db"
            )
            .fallbackToDestructiveMigration()
            .build()

}
