package com.pocketcocktails.pocketbar.data.room

import androidx.room.*

@Dao
interface RoomFavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(favorite: Favorite)

    @Insert
    suspend fun insertAll(vararg favorite: Favorite)

    @Update
    suspend fun update(favorite: Favorite)

    @Delete
    @Transaction
    suspend fun delete(favorite: Favorite)

    @Query("SELECT * FROM Favorite")
    suspend fun getAllFavorites(): List<Favorite>

    @Query("SELECT * FROM Favorite WHERE drinkId=(:id)")
    suspend fun getFavoriteById(id: String): Favorite

    @Query("DELETE FROM Favorite")
    fun cleaFavorites()
}