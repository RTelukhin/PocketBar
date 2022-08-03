package com.pocketcocktails.pocketbar.di

import com.pocketcocktails.pocketbar.domain.cocktail.interactions.impl.CocktailInteractionImpl
import com.pocketcocktails.pocketbar.domain.favorites.interactions.impl.FavoritesInteractionImpl
import com.pocketcocktails.pocketbar.domain.search.interactions.impl.SearchByBaseInteractionImpl
import com.pocketcocktails.pocketbar.domain.search.interactions.impl.SearchByQueryInteractionImpl
import com.pocketcocktails.pocketbar.domain.cocktail.interactions.CocktailInteraction
import com.pocketcocktails.pocketbar.domain.favorites.interactions.FavoritesInteraction
import com.pocketcocktails.pocketbar.domain.search.interactions.SearchByBaseInteraction
import com.pocketcocktails.pocketbar.domain.search.interactions.SearchByQueryInteraction
import dagger.Module
import dagger.Provides

@Module
object InteractionModule {

    @Provides
    fun provideCocktailInteraction(interactor: CocktailInteractionImpl): CocktailInteraction = interactor

    @Provides
    fun provideFavoritesInteraction(interactor: FavoritesInteractionImpl): FavoritesInteraction = interactor

    @Provides
    fun provideSearchByBaseInteraction(interactor: SearchByBaseInteractionImpl): SearchByBaseInteraction = interactor

    @Provides
    fun provideSearchByQueryInteraction(interactor: SearchByQueryInteractionImpl): SearchByQueryInteraction = interactor

}