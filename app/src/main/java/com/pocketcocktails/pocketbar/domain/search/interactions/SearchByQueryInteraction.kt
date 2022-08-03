package com.pocketcocktails.pocketbar.domain.search.interactions

import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.utils.Result
import kotlinx.coroutines.flow.Flow

interface SearchByQueryInteraction {

    fun searchDrink(searchText: String): Flow<Result<List<CocktailListItemModel>>>

    suspend fun changeFavorite(cocktail: CocktailListItemModel)
}