package com.pocketcocktails.pocketbar.presentation.search

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketcocktails.pocketbar.databinding.FragmentSearchByBaseBinding
import com.pocketcocktails.pocketbar.presentation.base.BaseFragment
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.presentation.search.action.UserActionSearchByBase
import com.pocketcocktails.pocketbar.presentation.search.adapter.SearchAdapter
import com.pocketcocktails.pocketbar.presentation.search.state.SearchViewState
import com.pocketcocktails.pocketbar.presentation.search.viewmodel.SearchByBaseViewModel
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.appComponent
import com.pocketcocktails.pocketbar.utils.setVisibility
import com.pocketcocktails.pocketbar.utils.showToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByBaseFragment : BaseFragment<FragmentSearchByBaseBinding>() {

    private lateinit var drinksAdapter: SearchAdapter

    private var cocktailBase = EMPTY_STRING

    private val args: SearchByBaseFragmentArgs by navArgs()

    override fun getViewBinding(): FragmentSearchByBaseBinding =
        FragmentSearchByBaseBinding.inflate(layoutInflater)

    override fun injectViewModel(appContext: Context) {
        appContext.appComponent.inject(this)
    }

    @Inject
    lateinit var searchByBaseViewModel: SearchByBaseViewModel

    override fun setupView() {
        cocktailBase = args.baseString
        searchByBaseViewModel.submit(UserActionSearchByBase.OnBaseChanged(cocktailBase))

        drinksAdapter = SearchAdapter(
            onItemClick = { cocktailListItem -> onItemClick(cocktailListItem) },
            onFavoriteClick = { cocktailListItem -> onFavoriteClick(cocktailListItem) }
        )

        with(receiver = binding) {
            cocktailsRecycler.layoutManager = LinearLayoutManager(requireContext())
            cocktailsRecycler.adapter = drinksAdapter
        }
    }

    override fun renderView() {
        searchByBaseViewModel.cocktailsByBaseViewState
            .flowWithLifecycle(lifecycle = lifecycle, minActiveState = Lifecycle.State.STARTED)
            .onEach { viewState -> renderView(viewState) }
            .launchIn(scope = lifecycleScope)
    }

    private fun renderView(viewState: SearchViewState) {
        when (viewState.items) {
            is SearchViewState.Items.Loading -> showLoading()
            is SearchViewState.Items.Drinks -> showDrinks(viewState.items)
            is SearchViewState.Items.Error -> showError(viewState.items)
            is SearchViewState.Items.Idle -> {}
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.cocktailsRecycler.setVisibility(false)
    }

    private fun showDrinks(result: SearchViewState.Items.Drinks) {
        binding.progressBar.setVisibility(false)
        binding.cocktailsRecycler.setVisibility(true)
        drinksAdapter.listCocktails = result.drinksList
    }

    private fun showError(result: SearchViewState.Items.Error) {
        binding.progressBar.setVisibility(false)
        binding.cocktailsRecycler.setVisibility(false)
        requireActivity().showToast(result.error ?: "Error")
    }

    private fun onItemClick(item: CocktailListItemModel) {
        val action =
            SearchByQueryFragmentDirections.actionFragmentSearchToFragmentCocktail(item.drinkId)
        binding.root.findNavController().navigate(action)
    }

    private fun onFavoriteClick(item: CocktailListItemModel) {
        searchByBaseViewModel.submit(UserActionSearchByBase.OnFavoritesChanged(item))
    }
}