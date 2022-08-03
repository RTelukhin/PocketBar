package com.pocketcocktails.pocketbar.di

import android.app.Application
import android.content.Context
import com.pocketcocktails.pocketbar.presentation.cocktail.CocktailFragment
import com.pocketcocktails.pocketbar.presentation.favorites.FavoritesFragment
import com.pocketcocktails.pocketbar.presentation.search.SearchByBaseFragment
import com.pocketcocktails.pocketbar.presentation.search.SearchByQueryFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        AppModule::class,
        InteractionModule::class,
        ViewModelModule::class
    ]
)
@ExperimentalCoroutinesApi
interface AppComponent {
    fun context(): Context
    fun applicationContext(): Application


    fun inject(fragment: CocktailFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: SearchByBaseFragment)
    fun inject(fragment: SearchByQueryFragment)
}