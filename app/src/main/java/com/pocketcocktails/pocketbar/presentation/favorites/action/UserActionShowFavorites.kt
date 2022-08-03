package com.pocketcocktails.pocketbar.presentation.favorites.action

import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel

sealed class UserActionShowFavorites {
    object ShowFavorites : UserActionShowFavorites()
    data class OnFavoritesChanged(val favoriteId: CocktailListItemModel) : UserActionShowFavorites()
}