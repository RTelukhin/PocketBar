package com.pocketcocktails.pocketbar.data.dto


import com.google.gson.annotations.SerializedName

data class DrinkListResponse(
    @SerializedName("drinks") val cocktailsList: List<DrinkInfoResponse>
)