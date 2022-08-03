package com.pocketcocktails.pocketbar.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(@PrimaryKey val drinkId: String)