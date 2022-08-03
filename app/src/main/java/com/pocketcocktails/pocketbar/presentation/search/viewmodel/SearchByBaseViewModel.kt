package com.pocketcocktails.pocketbar.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketcocktails.pocketbar.domain.search.interactions.SearchByBaseInteraction
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.presentation.search.action.UserActionSearchByBase
import com.pocketcocktails.pocketbar.presentation.search.state.SearchViewState
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByBaseViewModel @Inject constructor(private val searchByBaseInteraction: SearchByBaseInteraction) :
    ViewModel() {

    private val _userActionFlow = MutableSharedFlow<UserActionSearchByBase>(1)

    fun submit(userAction: UserActionSearchByBase) {
        _userActionFlow.tryEmit(userAction)
    }

    private val viewStateFlow =
        MutableStateFlow(SearchViewState(EMPTY_STRING, false, SearchViewState.Items.Idle))

    val cocktailsByBaseViewState: StateFlow<SearchViewState> by lazy {

        observeSearchAction().scan(
            initial = SearchViewState(),
            operation = { searchViewState, previousViewState ->
                return@scan previousViewState.invoke(searchViewState)
            })
            .onEach { viewState ->
                viewStateFlow.value = viewState
            }
            .launchIn(viewModelScope)
        return@lazy viewStateFlow
    }

    private fun observeSearchAction(): Flow<SearchPartialViewState> = observeSearchByBase()

    private fun observeSearchByBase(): Flow<SearchPartialViewState> = _userActionFlow
        .filterIsInstance<UserActionSearchByBase.OnBaseChanged>()
        .flatMapLatest { action ->
            performSearchByBase(base = action.base)
        }

    private fun performSearchByBase(base: String): Flow<SearchPartialViewState> = flow {
        emit(value = onBaseChanged(base = base))
        searchByBaseInteraction.searchDrinkByBase(base)
            .collect { value -> emit(value = onSearchResult(result = value)) }
    }

    init {
        val favoritePartialStateFlow: Flow<SearchPartialViewState> = _userActionFlow
            .filterIsInstance<UserActionSearchByBase.OnFavoritesChanged>()
            .flatMapLatest { action ->
                onFavoriteClick(action.favoriteId)
            }

        val basePartialStateFlow: Flow<SearchPartialViewState> = _userActionFlow
            .filterIsInstance<UserActionSearchByBase.OnBaseChanged>()
            .flatMapLatest { action ->
                performSearchByBase(base = action.base)
            }

        val allPartialStateFlow: Flow<SearchPartialViewState> =
            merge(basePartialStateFlow, favoritePartialStateFlow)

        allPartialStateFlow
            .scan(
                initial = SearchViewState(),
                operation = { searchViewState, previousViewState ->
                    return@scan previousViewState.invoke(searchViewState)
                })
            .onEach { viewState ->
                viewStateFlow.value = viewState
            }
            .launchIn(viewModelScope)
    }

    private fun onBaseChanged(base: String): SearchPartialViewState = { previousViewState ->
        val previousStateCopy =
            previousViewState.copy(query = base, items = SearchViewState.Items.Loading)
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

    private fun onFavoriteClick(cocktail: CocktailListItemModel): Flow<SearchPartialViewState> =
        flow {
            emit(value = onFavoriteChanged(cocktail = cocktail))
            searchByBaseInteraction.changeFavorite(cocktail)
        }

    private fun onFavoriteChanged(cocktail: CocktailListItemModel): SearchPartialViewState =
        { previousViewState ->
            val previousStateCopy = previousViewState.copy(isFavorite = cocktail.isFavorite)
            previousStateCopy
        }
}

typealias SearchPartialViewState = (SearchViewState) -> SearchViewState