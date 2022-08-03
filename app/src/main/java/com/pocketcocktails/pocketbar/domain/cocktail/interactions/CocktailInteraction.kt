package com.pocketcocktails.pocketbar.domain.cocktail.interactions

import com.pocketcocktails.pocketbar.presentation.model.CocktailInfoModel
import com.pocketcocktails.pocketbar.utils.Result

interface CocktailInteraction {

    suspend fun getDrinkById(id: String): Result<CocktailInfoModel>
}