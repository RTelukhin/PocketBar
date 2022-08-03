package com.pocketcocktails.pocketbar.domain.favorites.interactions

import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.utils.Result

interface FavoritesInteraction {

    suspend fun showFavoritesDrink(): Result<List<CocktailListItemModel>>

    suspend fun deleteFavorite(cocktail: CocktailListItemModel): Result<String>

}