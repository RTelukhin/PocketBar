package com.pocketcocktails.pocketbar.presentation.cocktail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocketcocktails.pocketbar.databinding.FragmentCocktailBinding
import com.pocketcocktails.pocketbar.presentation.cocktail.adapter.CocktailIngredientsAdapter
import com.pocketcocktails.pocketbar.presentation.base.BaseFragment
import com.pocketcocktails.pocketbar.presentation.cocktail.viewmodel.CocktailViewModel
import com.pocketcocktails.pocketbar.presentation.cocktail.state.CocktailViewState
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import com.pocketcocktails.pocketbar.utils.Constants.TEST_LOG_TAG
import com.pocketcocktails.pocketbar.utils.appComponent
import com.pocketcocktails.pocketbar.utils.load
import com.pocketcocktails.pocketbar.utils.setVisibility
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class CocktailFragment : BaseFragment<FragmentCocktailBinding>() {

    private var ingredientAdapter = CocktailIngredientsAdapter()

    private var idCocktail = EMPTY_STRING

    @Inject
    lateinit var cocktailViewModel: CocktailViewModel

    override fun getViewBinding(): FragmentCocktailBinding = FragmentCocktailBinding.inflate(layoutInflater)

    private val args: CocktailFragmentArgs by navArgs()

    override fun injectViewModel(appContext: Context) {
        requireContext().appComponent.inject(this)
    }

    override fun setupView() {
        with(receiver = binding) {
//            cocktailViewModel.mIdCocktail = idCocktail
            cocktailViewModel.mIdCocktail = args.drinkId
            ingredientsRecycler.layoutManager = LinearLayoutManager(requireContext())
            ingredientsRecycler.adapter = ingredientAdapter
            cocktailViewModel.userActionChannel.tryEmit(com.pocketcocktails.pocketbar.presentation.cocktail.action.UserActionSearchById.OnIdChanged)
        }
    }

    override fun renderView() {
        lifecycleScope
            .launchWhenStarted {
                cocktailViewModel
                    .cocktailViewState
                    .collect { cocktailViewState -> showData(cocktailViewState.cocktail) }
            }
    }

    private fun showData(viewState: CocktailViewState.Item) {
        when (viewState) {
            is CocktailViewState.Item.Loading -> binding.cocktailProgress.setVisibility(true)
            is CocktailViewState.Item.Cocktail -> {
                binding.cocktailProgress.setVisibility(false)
                if (viewState.cocktailItem.drinkThumb != null) {
                    binding.nameCocktail.text = viewState.cocktailItem.name
                    binding.drinkImage.load(viewState.cocktailItem.drinkThumb)
                    ingredientAdapter.listIngrid = viewState.cocktailItem.ingredient
                }
            }
            is CocktailViewState.Item.Error -> {
                Toast.makeText(requireActivity(), "${viewState.error}", Toast.LENGTH_LONG).show()
            }
        }
    }
}