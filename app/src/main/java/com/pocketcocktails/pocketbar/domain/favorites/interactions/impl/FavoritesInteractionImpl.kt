package com.pocketcocktails.pocketbar.domain.favorites.interactions.impl

import com.pocketcocktails.pocketbar.data.room.Favorite
import com.pocketcocktails.pocketbar.data.room.FavoriteDatabase
import com.pocketcocktails.pocketbar.domain.CocktailsRepository
import com.pocketcocktails.pocketbar.domain.favorites.interactions.FavoritesInteraction
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.utils.Result
import javax.inject.Inject

class FavoritesInteractionImpl @Inject constructor(
    private val cocktailsRepository: CocktailsRepository,
    private val favoriteDatabase: FavoriteDatabase
) : FavoritesInteraction {

    override suspend fun showFavoritesDrink(): Result<List<CocktailListItemModel>> {
        val favorites = ArrayList<CocktailListItemModel>()
        return try {
            val data = favoriteDatabase.getFavoriteDAO().getAllFavorites()
            data.forEach {
                val item = cocktailsRepository.getFavoriteById(id = it.drinkId)
                favorites.add(item)
            }
            Result.Success(favorites)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }

    override suspend fun deleteFavorite(cocktail: CocktailListItemModel): Result<String> {
        return try {
            val itemForDelete = Favorite(cocktail.drinkId)
            favoriteDatabase.getFavoriteDAO().delete(itemForDelete)
            Result.Success(cocktail.drinkId)
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}
