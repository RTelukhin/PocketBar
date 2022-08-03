package com.pocketcocktails.pocketbar.presentation.search.state

import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING

data class SearchViewState(val query: String = EMPTY_STRING, val isFavorite: Boolean = false, val items: Items = Items.Idle) {

    sealed class Items {
        object Idle : Items()
        object Loading : Items()
        data class Drinks(val drinksList: List<CocktailListItemModel>) : Items()
        data class Error(val error: String?) : Items()
    }
}