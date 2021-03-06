package com.ardianeffendi.githubclient.db

import android.database.Cursor
import androidx.room.*
import com.ardianeffendi.githubclient.models.FavoriteUsers
import com.ardianeffendi.githubclient.utils.Constants.Companion.TABLE_FAVORITE

@Dao
interface FavUsersDao {

    @Query("SELECT * FROM $TABLE_FAVORITE")
    fun getAllFavoriteCursor(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteCursor(user: FavoriteUsers): Long

    @Query("SELECT * FROM $TABLE_FAVORITE WHERE id=:id")
    fun getFavoritesByIdCursor(id: String): Cursor

    @Query("DELETE FROM favorite_users WHERE id=:id")
    fun deleteFavoriteCursor(id: String): Int

    @Update
    fun updateFavoriteCursor(user: FavoriteUsers): Int
}