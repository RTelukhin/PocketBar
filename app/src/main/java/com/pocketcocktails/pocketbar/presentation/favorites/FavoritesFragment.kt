package com.pocketcocktails.pocketbar.presentation.favorites

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.databinding.FragmentFavoritesBinding
import com.pocketcocktails.pocketbar.presentation.favorites.action.UserActionShowFavorites
import com.pocketcocktails.pocketbar.presentation.favorites.adapter.FavoritesAdapter
import com.pocketcocktails.pocketbar.presentation.base.BaseFragment
import com.pocketcocktails.pocketbar.presentation.cocktail.CocktailFragment
import com.pocketcocktails.pocketbar.presentation.favorites.viewmodel.FavoritesViewModel
import com.pocketcocktails.pocketbar.presentation.favorites.state.FavoritesViewState
import com.pocketcocktails.pocketbar.utils.appComponent
import com.pocketcocktails.pocketbar.utils.setVisibility
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    private lateinit var favoritesAdapter: FavoritesAdapter

    @Inject
    lateinit var favoritesViewModel: FavoritesViewModel

    override fun getViewBinding(): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(layoutInflater)

    override fun injectViewModel(appContext: Context) {
        appContext.appComponent.inject(this)
    }

    override fun setupView() {
        favoritesViewModel.userActionChannel.tryEmit(UserActionShowFavorites.ShowFavorites)
        favoritesAdapter = FavoritesAdapter(
            onItemClick = { cocktailListItem -> onItemClick(cocktailListItem) },
            onFavoriteClick = { cocktailListItem -> onFavoriteClick(cocktailListItem) }
        )
        with(receiver = binding) {
            favoritesRecycler.layoutManager = LinearLayoutManager(requireContext())
            favoritesRecycler.adapter = favoritesAdapter
        }
    }

    override fun renderView() {
        favoritesViewModel.favoriteCocktailsViewState
            .flowWithLifecycle(lifecycle = lifecycle, minActiveState = Lifecycle.State.STARTED)
            .onEach { viewState ->
                when (val state = viewState.items) {
                    is FavoritesViewState.Items.Loading -> showLoading()
                    is FavoritesViewState.Items.Drinks -> showDrinks(state)
                    is FavoritesViewState.Items.Error -> showError(state)
                    is FavoritesViewState.Items.Idle -> {}
                }
            }
            .launchIn(scope = lifecycleScope)
    }

    private fun showLoading() {
        binding.progressBar.setVisibility(true)
        binding.infoTextView.setVisibility(false)
        binding.favoritesRecycler.setVisibility(false)
    }

    private fun showDrinks(result: FavoritesViewState.Items.Drinks) {
        binding.progressBar.setVisibility(false)
        binding.infoTextView.setVisibility(false)
        binding.favoritesRecycler.setVisibility(true)
        favoritesAdapter.listCocktails = result.drinksList
    }

    private fun showError(result: FavoritesViewState.Items.Error) {
        binding.progressBar.setVisibility(false)
        binding.favoritesRecycler.setVisibility(false)
        binding.infoTextView.setVisibility(true)
        binding.infoTextView.text = result.error
    }

    private fun onItemClick(item: CocktailListItemModel) {}

    private fun onFavoriteClick(item: CocktailListItemModel) {
        favoritesViewModel.userActionChannel.tryEmit(UserActionShowFavorites.OnFavoritesChanged(item))
    }
}