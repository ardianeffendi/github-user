package com.ardianeffendi.consumer_github.repository

import com.ardianeffendi.consumer_github.api.RetrofitInstance
import com.ardianeffendi.consumer_github.db.FavoriteUsersDatabase
import com.ardianeffendi.consumer_github.models.FavoriteUsers

class UsersRepository(
    val db: FavoriteUsersDatabase
) {

    suspend fun getUser(username: String) = RetrofitInstance.api.getUser(username)

    suspend fun getDetailUser(username: String) = RetrofitInstance.apiDetail.getDetailUser(username)

    suspend fun getListFollowing(username: String) =
        RetrofitInstance.apiDetail.getUserFollowing(username)

    suspend fun getListFollowers(username: String) =
        RetrofitInstance.apiDetail.getUserFollowers(username)

    suspend fun insert(user: FavoriteUsers) = db.getFavUsersDao().insert(user)

    fun getFavUsers() = db.getFavUsersDao().getAllUsers()

    fun getCertainUser(id: Int) = db.getFavUsersDao().getCertainUser(id)

    suspend fun delete(user: FavoriteUsers) = db.getFavUsersDao().deleteUser(user)
}