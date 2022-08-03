package com.pocketcocktails.pocketbar.presentation.model


data class CocktailListItemModel(
    val drinkId: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val isFavorite: Boolean
)