package com.pocketcocktails.pocketbar.presentation.search

import android.content.Context
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketcocktails.pocketbar.databinding.FragmentSearchBinding
import com.pocketcocktails.pocketbar.presentation.base.BaseFragment
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.presentation.search.action.UserActionSearchByQuery
import com.pocketcocktails.pocketbar.presentation.search.adapter.SearchAdapter
import com.pocketcocktails.pocketbar.presentation.search.state.SearchViewState
import com.pocketcocktails.pocketbar.presentation.search.viewmodel.SearchByQueryViewModel
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.appComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchByQueryFragment : BaseFragment<FragmentSearchBinding>() {

    private lateinit var drinksAdapter: SearchAdapter

    @Inject
    lateinit var searchByQueryViewModel: SearchByQueryViewModel

    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)

    override fun injectViewModel(appContext: Context) {
        appContext.appComponent.inject(this)
    }

    override fun setupView() =
        with(receiver = binding) {
            drinksAdapter = SearchAdapter(
                onItemClick = { cocktailListItem -> onItemClick(cocktailListItem) },
                onFavoriteClick = { cocktailListItem -> onFavoriteClick(cocktailListItem) }
            )
            drinksAdapter.setHasStableIds(true)
            cocktailsRecycler.layoutManager = LinearLayoutManager(requireContext())
            cocktailsRecycler.adapter = drinksAdapter
            searchView.isIconified = false
            searchView.setOnQueryTextListener(
                object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean = false
                    override fun onQueryTextChange(newText: String): Boolean {
                        searchByQueryViewModel.submit(UserActionSearchByQuery.OnQueryChanged(newText))
                        return false
                    }
                })
        }

    override fun renderView() {
        lifecycleScope
            .launchWhenStarted {
                searchByQueryViewModel
                    .searchViewState
                    .collect {
                        when (val state = it.items) {
                            is SearchViewState.Items.Loading -> showLoading()
                            is SearchViewState.Items.Drinks -> showDrinks(state)
                            is SearchViewState.Items.Error -> showError(state)
                            is SearchViewState.Items.Idle -> showIdle()
                        }
                    }
            }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.infoTextView.visibility = View.GONE
        binding.cocktailsRecycler.visibility = View.GONE
    }

    private fun showDrinks(result: SearchViewState.Items.Drinks) {
        binding.progressBar.visibility = View.GONE
        binding.infoTextView.visibility = View.GONE
        binding.cocktailsRecycler.visibility = View.VISIBLE
        drinksAdapter.listCocktails = result.drinksList
    }

    private fun showError(result: SearchViewState.Items.Error) {
        binding.progressBar.visibility = View.GONE
        binding.cocktailsRecycler.visibility = View.GONE
        binding.infoTextView.visibility = View.VISIBLE
        binding.infoTextView.text = result.error
    }

    private fun showIdle() {
        binding.progressBar.visibility = View.GONE
        binding.infoTextView.visibility = View.VISIBLE
        binding.infoTextView.text = "Input text"
        binding.cocktailsRecycler.visibility = View.GONE
    }

    private fun onItemClick(item: CocktailListItemModel) {
        val id = item.drinkId
        val action = SearchByQueryFragmentDirections.actionFragmentSearchToFragmentCocktail(id)
        binding.root.findNavController().navigate(action)
    }

    private fun onFavoriteClick(item: CocktailListItemModel) {
        searchByQueryViewModel.submit(UserActionSearchByQuery.OnFavoritesChanged(item))
    }
}