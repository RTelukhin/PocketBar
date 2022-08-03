package com.pocketcocktails.pocketbar.presentation.model

import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING

data class IngredientModel(
    val name: String= EMPTY_STRING ,
    val measure: String = EMPTY_STRING ,
)