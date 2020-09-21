package com.ardianeffendi.githubuser.helper

import android.annotation.SuppressLint
import android.database.Cursor
import android.util.Log
import androidx.room.util.CursorUtil
import com.ardianeffendi.githubuser.models.FavoriteUsers

object MappingHelper {

    @SuppressLint("RestrictedApi")
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<FavoriteUsers> {
        val favoriteList = ArrayList<FavoriteUsers>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = CursorUtil.getColumnIndexOrThrow(userCursor, "id")
                val username = CursorUtil.getColumnIndexOrThrow(userCursor, "username")
                val name = CursorUtil.getColumnIndexOrThrow(userCursor, "name")
                val avatar = CursorUtil.getColumnIndexOrThrow(userCursor, "avatar")
                favoriteList.add(FavoriteUsers(userCursor.getInt(id),
                    userCursor.getString(username),
                    userCursor.getString(name),
                    userCursor.getString(avatar)))
            }
        }
        return favoriteList
    }

    @SuppressLint("RestrictedApi")
    fun mapCursorToObject(userCursor: Cursor?): FavoriteUsers {
        var favorite = FavoriteUsers()
        userCursor?.apply {
            if (userCursor.moveToFirst()) {
                val id = CursorUtil.getColumnIndexOrThrow(userCursor, "id")
                val username = CursorUtil.getColumnIndexOrThrow(userCursor, "username")
                val name = CursorUtil.getColumnIndexOrThrow(userCursor, "name")
                val avatar = CursorUtil.getColumnIndexOrThrow(userCursor, "avatar")
                favorite = FavoriteUsers(
                    userCursor.getInt(id),
                    userCursor.getString(username),
                    userCursor.getString(name),
                    userCursor.getString(avatar)
                )
            }
        }
        Log.d("isUserExist", "Favorite => $favorite")
        return favorite
    }

}