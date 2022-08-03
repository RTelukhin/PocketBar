package com.pocketcocktails.pocketbar.domain.search.interactions.impl

import com.pocketcocktails.pocketbar.data.room.Favorite
import com.pocketcocktails.pocketbar.data.room.FavoriteDatabase
import com.pocketcocktails.pocketbar.domain.CocktailsRepository
import com.pocketcocktails.pocketbar.domain.search.interactions.SearchByBaseInteraction
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.utils.Constants
import com.pocketcocktails.pocketbar.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SearchByBaseInteractionImpl @Inject constructor(
    private val cocktailsRepository: CocktailsRepository,
    private val favoriteDatabase: FavoriteDatabase
) : SearchByBaseInteraction {

    override suspend fun searchDrinkByBase(searchBase: String): Flow<Result<List<CocktailListItemModel>>> =
        flow {
            if (searchBase.isEmpty()) {
                emit(value = Result.Success(emptyList()))
            } else {
                try {
                    var data = cocktailsRepository.getDrinkByName(searchBase)

                    val favorites: ArrayList<Favorite> = ArrayList(favoriteDatabase.getFavoriteDAO().getAllFavorites())

                    data = data.map { cocktail ->
                        var item = cocktail
                        favorites.map { favorite ->
                            if (cocktail.drinkId == favorite.drinkId) {
                                item = cocktail.copy(isFavorite = true)
                            }
                        }
                        item
                    }
                    emit(value = Result.Success(data))
                } catch (e: Throwable) {
                    emit(value = Result.Failure(e))
                }
            }
        }


    override suspend fun changeFavorite(cocktail: CocktailListItemModel) {
        val favorites: ArrayList<Favorite> =
            ArrayList(favoriteDatabase.getFavoriteDAO().getAllFavorites())
        val idCocktail = cocktail.drinkId

        if (cocktail.isFavorite) {
            val deletedItem = favorites.find { item -> item.drinkId == idCocktail }
            favorites.remove(deletedItem)
            val delete = Favorite(cocktail.drinkId)
            favoriteDatabase.getFavoriteDAO().delete(delete)
        } else {
            val favorite = Favorite(drinkId = idCocktail)
            favoriteDatabase.getFavoriteDAO().add(favorite)
        }
    }
}