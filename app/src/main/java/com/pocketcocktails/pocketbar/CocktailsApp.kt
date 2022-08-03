package com.pocketcocktails.pocketbar

import android.app.Application
import com.pocketcocktails.pocketbar.BuildConfig.DEBUG
import com.pocketcocktails.pocketbar.di.AppComponent
import com.pocketcocktails.pocketbar.di.AppModule
import com.pocketcocktails.pocketbar.di.DaggerAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
class CocktailsApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (DEBUG) Timber.plant(Timber.DebugTree())

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}