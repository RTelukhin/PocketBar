package com.pocketcocktails.pocketbar.data.network

import com.pocketcocktails.pocketbar.data.dto.DrinkListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search.php?")
    suspend fun getDrinkByName(@Query("s") searchName: String): DrinkListResponse

    @GET("lookup.php?")
    suspend fun getDrinkById(@Query("i") searchId: String): DrinkListResponse

    @GET("filter.php?")
    suspend fun getDrinkByIngredient(@Query("i") searchIngredient: String): DrinkListResponse

    @GET("lookup.php?")
    suspend fun getTenRandomDrinks(@Query("i") id: String): DrinkListResponse

    @GET("lookup.php?")
    suspend fun getHardSoftDrinks(@Query("a") type: String): DrinkListResponse

}