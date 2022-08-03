package com.pocketcocktails.pocketbar.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pocketcocktails.pocketbar.R
import com.pocketcocktails.pocketbar.databinding.FragmentHomeBinding
import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.rumBase.setOnClickListener(this)
        binding.ginBase.setOnClickListener(this)
        binding.tequilaBase.setOnClickListener(this)
        binding.whiskeyBase.setOnClickListener(this)
        binding.vodkaBase.setOnClickListener(this)
        binding.brandyBase.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(p0: View) {
        val baseText = when (p0) {
            binding.rumBase -> getString(R.string.rum_text)
            binding.ginBase -> getString(R.string.gin_text)
            binding.tequilaBase -> getString(R.string.tequila_text)
            binding.whiskeyBase -> getString(R.string.whiskey_text)
            binding.vodkaBase -> getString(R.string.vodka_text)
            binding.brandyBase -> getString(R.string.brandy_text)
            else -> EMPTY_STRING
        }
        val action = HomeFragmentDirections.actionFragmentHomeToFragmentCocktailByBase(baseText)
        binding.root.findNavController().navigate(action)
    }

}