package com.pocketcocktails.pocketbar.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketcocktails.pocketbar.domain.search.interactions.SearchByQueryInteraction
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.presentation.search.action.UserActionSearchByQuery
import com.pocketcocktails.pocketbar.presentation.search.state.SearchViewState
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByQueryViewModel @Inject constructor(private val searchByQueryInteraction: SearchByQueryInteraction) :
    ViewModel() {

    private val _userActionFlow = MutableSharedFlow<UserActionSearchByQuery>(1)

    fun submit(userAction: UserActionSearchByQuery) {
        _userActionFlow.tryEmit(userAction)
    }

    private val mutableStateFlow =
        MutableStateFlow(SearchViewState(EMPTY_STRING, false, SearchViewState.Items.Idle))

    val searchViewState: StateFlow<SearchViewState>
        get() = mutableStateFlow

    private fun performSearchByQuery(queryText: String): Flow<SearchPartialViewState> = flow {
        emit(value = onQueryChanged(queryText = queryText))
        delay(1000L)
        searchByQueryInteraction.searchDrink(queryText)
            .collect { value -> emit(value = onSearchResult(result = value)) }
    }

    init {
        val favoritePartialStateFlow: Flow<SearchPartialViewState> = _userActionFlow
            .filterIsInstance<UserActionSearchByQuery.OnFavoritesChanged>()
            .flatMapLatest { action ->
                onFavoriteClick(action.favoriteId)
            }

        val queryPartialStateFlow: Flow<SearchPartialViewState> = _userActionFlow
            .filterIsInstance<UserActionSearchByQuery.OnQueryChanged>()
            .flatMapLatest { action ->
                performSearchByQuery(queryText = action.searchText)
            }

        val allPartialStateFlow: Flow<SearchPartialViewState> =
            merge(queryPartialStateFlow, favoritePartialStateFlow)

        allPartialStateFlow
            .scan(
                initial = SearchViewState(),
                operation = { searchViewState, previousViewState ->
                    return@scan previousViewState.invoke(searchViewState)
                })
            .onEach { viewState ->
                mutableStateFlow.value = viewState
            }
            .launchIn(viewModelScope)
    }

    private fun onFavoriteClick(cocktail: CocktailListItemModel): Flow<SearchPartialViewState> =
        flow {
            emit(value = onFavoriteChanged(cocktail = cocktail))
            searchByQueryInteraction.changeFavorite(cocktail)
        }

    private fun onQueryChanged(queryText: String): SearchPartialViewState = { previousViewState ->
        val previousStateCopy =
            previousViewState.copy(query = queryText, items = SearchViewState.Items.Loading)
        previousStateCopy
    }

    private fun onFavoriteChanged(cocktail: CocktailListItemModel): SearchPartialViewState =
        { previousViewState ->
            val previousStateCopy = previousViewState.copy(isFavorite = cocktail.isFavorite)
            previousStateCopy
        }

    private fun onSearchResult(result: Result<List<CocktailListItemModel>>): SearchPartialViewState =
        { previousViewState ->
            val items = when (result) {
                is Result.Success -> SearchViewState.Items.Drinks(result.data)
                is Result.Failure -> SearchViewState.Items.Error(result.exception.message)
            }
            val onSearchResult = previousViewState.copy(items = items)
            onSearchResult
        }
}