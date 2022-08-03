package com.pocketcocktails.pocketbar.di

import com.pocketcocktails.pocketbar.domain.CocktailsRepository
import com.pocketcocktails.pocketbar.data.CocktailsRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {

    @Provides
    fun provideMainRepositoryImpl(repository: CocktailsRepositoryImpl): CocktailsRepository =
        repository

}
