package com.ardianeffendi.githubuser.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users")
data class FavoriteUsers(
    @PrimaryKey var id: Int = 0,
    @ColumnInfo(name = "username") var login: String? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "avatar") var avatar_url: String? = null
)