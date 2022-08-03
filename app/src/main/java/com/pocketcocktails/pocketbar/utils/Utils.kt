package com.pocketcocktails.pocketbar.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.pocketcocktails.pocketbar.CocktailsApp
import com.pocketcocktails.pocketbar.di.AppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
val Context.appComponent: AppComponent
    get() = when (this) {
        is CocktailsApp -> appComponent
        else -> this.applicationContext.appComponent
    }

fun ImageView.load(imageAddress: String) {
    Glide.with(this)
        .load(imageAddress)
        .into(this)
}

fun View.setVisibility(isNeedShow: Boolean) {
    this.visibility = if(isNeedShow) View.VISIBLE else View.GONE
}

fun Context.showToast(info: String) {
    Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
}
