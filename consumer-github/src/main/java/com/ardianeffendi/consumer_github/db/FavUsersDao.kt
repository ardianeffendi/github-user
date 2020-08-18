package com.ardianeffendi.consumer_github.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ardianeffendi.consumer_github.models.FavoriteUsers

@Dao
interface FavUsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: FavoriteUsers): Long

    @Query("SELECT * FROM favorite_users")
    fun getAllUsers(): LiveData<List<FavoriteUsers>>

    @Query("SELECT * FROM favorite_users WHERE id = :id ")
    fun getCertainUser(id: Int): LiveData<List<FavoriteUsers>>

    @Delete
    suspend fun deleteUser(user: FavoriteUsers)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteCursor(user: FavoriteUsers): Long

    @Query("DELETE FROM favorite_users WHERE id=:id")
    fun deleteFavoriteCursor(id: String): Int

    @Update
    fun updateFavoriteCursor(user: FavoriteUsers): Int
}