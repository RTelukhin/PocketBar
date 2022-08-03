package com.pocketcocktails.pocketbar.di

import androidx.lifecycle.ViewModel
import com.pocketcocktails.pocketbar.presentation.cocktail.viewmodel.CocktailViewModel
import com.pocketcocktails.pocketbar.presentation.favorites.viewmodel.FavoritesViewModel
import com.pocketcocktails.pocketbar.presentation.search.viewmodel.SearchByBaseViewModel
import com.pocketcocktails.pocketbar.presentation.search.viewmodel.SearchByQueryViewModel
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
object ViewModelModule {
    @Provides
    fun provideCocktailViewModel(viewModel: CocktailViewModel): ViewModel = viewModel
    @Provides
    fun provideFavoritesViewModel(viewModel: FavoritesViewModel): ViewModel = viewModel
    @Provides
    fun provideSearchByBaseViewModel(viewModel: SearchByBaseViewModel): ViewModel = viewModel
    @Provides
    fun provideSearchByQueryViewModel(viewModel: SearchByQueryViewModel): ViewModel = viewModel

}