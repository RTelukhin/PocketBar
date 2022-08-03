package com.pocketcocktails.pocketbar.presentation.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketcocktails.pocketbar.domain.favorites.interactions.FavoritesInteraction
import com.pocketcocktails.pocketbar.presentation.favorites.action.UserActionShowFavorites
import com.pocketcocktails.pocketbar.presentation.favorites.state.FavoritesViewState
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject


typealias FavoritesPartialViewState = (FavoritesViewState) -> FavoritesViewState

@ExperimentalCoroutinesApi
class FavoritesViewModel @Inject constructor(private val searchInteraction: FavoritesInteraction) :
    ViewModel() {
    val userActionChannel = MutableSharedFlow<UserActionShowFavorites>(1)

    private val mutableStateFlow =
        MutableStateFlow(FavoritesViewState(FavoritesViewState.Items.Idle))

    val favoriteCocktailsViewState: SharedFlow<FavoritesViewState>
        get() = mutableStateFlow

    private fun performShowFavorites(): Flow<FavoritesPartialViewState> = flow {
        emit(value = onFavoritesLoading())
        val result = searchInteraction.showFavoritesDrink()
        emit(value = onFavoritesResult(result = result))
    }

    private fun onFavoriteClick(cocktail: CocktailListItemModel): Flow<FavoritesPartialViewState> =
        flow {
            val result = searchInteraction.deleteFavorite(cocktail)
            emit(value = onFavoriteDelete(deleteResult = result))
        }

    init {
        val favoritesPartialStateFlow: Flow<FavoritesPartialViewState> = userActionChannel
            .filterIsInstance<UserActionShowFavorites.ShowFavorites>()
            .flatMapLatest { action ->
                performShowFavorites()
            }

        val changeFavoritePartialStateFlow: Flow<FavoritesPartialViewState> = userActionChannel
            .filterIsInstance<UserActionShowFavorites.OnFavoritesChanged>()
            .flatMapLatest { action ->
                onFavoriteClick(action.favoriteId)
            }

        val allPartialStateFlow: Flow<FavoritesPartialViewState> =
            merge(favoritesPartialStateFlow, changeFavoritePartialStateFlow)

        allPartialStateFlow
            .scan(
                initial = FavoritesViewState(),
                operation = { favoritesViewState, previousViewState ->
                    return@scan previousViewState.invoke(favoritesViewState)
                })
            .onEach { viewState ->
                mutableStateFlow.value = viewState
            }
            .launchIn(viewModelScope)
    }

    private fun onFavoritesLoading(): FavoritesPartialViewState =
        { previousViewState ->
            val previousStateCopy = previousViewState.copy(items = FavoritesViewState.Items.Loading)
            previousStateCopy
        }

    private fun onFavoritesResult(result: Result<List<CocktailListItemModel>>): FavoritesPartialViewState =
        { previousViewState ->
            val items = when (result) {
                is Result.Success -> FavoritesViewState.Items.Drinks(result.data)
                is Result.Failure -> FavoritesViewState.Items.Error(result.exception.message)
            }
            val onFavoritesResult = previousViewState.copy(items = items)
            onFavoritesResult
        }

    private fun onFavoriteDelete(deleteResult: Result<String>): FavoritesPartialViewState =
        { previousViewState ->
            when (deleteResult) {
                is Result.Success -> {
                    var newViewState = previousViewState
                    if (previousViewState.items is FavoritesViewState.Items.Drinks) {
                        val items = ArrayList(previousViewState.items.drinksList)
                        val newList = items.filter { item -> item.drinkId != deleteResult.data }
                        newViewState =
                            previousViewState.copy(items = FavoritesViewState.Items.Drinks(newList))
                    }
                    newViewState
                }
                else -> previousViewState
            }
        }
}