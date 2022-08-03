package com.pocketcocktails.pocketbar.presentation.cocktail.state

import com.pocketcocktails.pocketbar.presentation.model.CocktailInfoModel

data class CocktailViewState(val cocktail: Item = Item.Loading) {

    sealed class Item {
        object Loading : Item()
        data class Cocktail(val cocktailItem: CocktailInfoModel) : Item()
        data class Error(val error: String?) : Item()
    }
}