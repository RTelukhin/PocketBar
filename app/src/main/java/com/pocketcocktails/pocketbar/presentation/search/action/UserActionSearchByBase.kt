package com.pocketcocktails.pocketbar.presentation.search.action

import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel

sealed class UserActionSearchByBase {
    data class OnBaseChanged(val base: String) : UserActionSearchByBase()
    data class OnFavoritesChanged(val favoriteId: CocktailListItemModel) : UserActionSearchByBase()
}