package com.pocketcocktails.pocketbar.presentation.cocktail.action

sealed class UserActionSearchById {
    object OnIdChanged: UserActionSearchById()
}