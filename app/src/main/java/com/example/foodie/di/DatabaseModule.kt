package com.example.foodie.di

import android.content.Context
import androidx.room.Room
import com.example.foodie.FOODIE_DATABASE
import com.example.foodie.data.database.FoodieDatabase
import com.example.foodie.data.database.RecipesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): FoodieDatabase = Room.databaseBuilder(
        context,
        FoodieDatabase::class.java,
        FOODIE_DATABASE
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: FoodieDatabase): RecipesDao = database.recipesDao()
}