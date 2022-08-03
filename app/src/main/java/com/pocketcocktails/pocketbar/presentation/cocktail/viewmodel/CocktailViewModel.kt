package com.pocketcocktails.pocketbar.presentation.cocktail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketcocktails.pocketbar.domain.cocktail.interactions.CocktailInteraction
import com.pocketcocktails.pocketbar.presentation.cocktail.action.UserActionSearchById
import com.pocketcocktails.pocketbar.presentation.cocktail.state.CocktailViewState
import com.pocketcocktails.pocketbar.presentation.model.CocktailInfoModel
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CocktailViewModel @Inject constructor(private val searchInteraction: CocktailInteraction) :
    ViewModel() {

    var mIdCocktail = EMPTY_STRING

    val userActionChannel = MutableSharedFlow<UserActionSearchById>(1)

    private val mutableSelectedItem =
        MutableStateFlow(CocktailViewState(CocktailViewState.Item.Loading))

    val cocktailViewState: StateFlow<CocktailViewState>
        get() = mutableSelectedItem

    private fun performSearchById(): Flow<CocktailPartialViewState> = flow {
        emit(value = onIdChanged())
        delay(1000L)
        val result = searchInteraction.getDrinkById(mIdCocktail)
        emit(value = onSearchResult(result = result))
    }

    init {
        val idSearchPartialStateFlow: Flow<CocktailPartialViewState> = userActionChannel
            .flatMapLatest { performSearchById() }

        idSearchPartialStateFlow
            .scan(
                initial = CocktailViewState(),
                operation = { cocktailViewState, previousViewState ->
                    return@scan previousViewState.invoke(cocktailViewState)
                })
            .onEach { viewState -> mutableSelectedItem.value = viewState }
            .launchIn(viewModelScope)
    }

    private fun onIdChanged(): CocktailPartialViewState = { previousViewState ->
        val previousStateCopy = previousViewState.copy(cocktail = CocktailViewState.Item.Loading)
        previousStateCopy
    }

    private fun onSearchResult(result: Result<CocktailInfoModel>): CocktailPartialViewState =
        { previousViewState ->
            val items = when (result) {
                is Result.Success -> CocktailViewState.Item.Cocktail(result.data)
                is Result.Failure -> CocktailViewState.Item.Error(result.exception.message)
            }
            val onSearchResult = previousViewState.copy(cocktail = items)
            onSearchResult
        }
}

typealias CocktailPartialViewState = (CocktailViewState) -> CocktailViewState