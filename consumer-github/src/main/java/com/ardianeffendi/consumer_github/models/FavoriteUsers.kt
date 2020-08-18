package com.ardianeffendi.consumer_github.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users")
data class FavoriteUsers(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "username") val login: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "avatar") val avatar_url: String
)