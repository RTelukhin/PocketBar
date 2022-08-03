package com.pocketcocktails.pocketbar.domain

import com.pocketcocktails.pocketbar.presentation.model.CocktailInfoModel
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel

interface CocktailsRepository {
    suspend fun getDrinkByName(searchText: String): List<CocktailListItemModel>
    suspend fun getDrinkById(id: String): CocktailInfoModel
    suspend fun getDrinkByBase(baseName: String): List<CocktailListItemModel>
    suspend fun getFavoriteById(id: String): CocktailListItemModel
}