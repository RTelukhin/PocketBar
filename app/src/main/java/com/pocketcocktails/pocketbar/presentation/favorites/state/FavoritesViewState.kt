package com.pocketcocktails.pocketbar.presentation.favorites.state

import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel

data class FavoritesViewState(val items: Items = Items.Idle, val isFavorite: Boolean = false) {

    sealed class Items {
        object Idle : Items()
        object Loading : Items()
        data class Drinks(val drinksList: List<CocktailListItemModel>) : Items()
        data class Error(val error: String?) : Items()
    }
}