package com.pocketcocktails.pocketbar.presentation.search.action

import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel

sealed class UserActionSearchByQuery {
    data class OnQueryChanged(val searchText: String) : UserActionSearchByQuery()
    data class OnFavoritesChanged(val favoriteId: CocktailListItemModel) : UserActionSearchByQuery()
}