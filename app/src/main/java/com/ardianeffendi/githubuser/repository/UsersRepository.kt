package com.ardianeffendi.githubuser.repository

import com.ardianeffendi.githubuser.api.RetrofitInstance
import com.ardianeffendi.githubuser.db.FavoriteUsersDatabase
import com.ardianeffendi.githubuser.models.FavoriteUsers

class UsersRepository(
    val db: FavoriteUsersDatabase
) {

    // Network Call
    suspend fun getUser(username: String) = RetrofitInstance.api.getUser(username)

    suspend fun getDetailUser(username: String) = RetrofitInstance.apiDetail.getDetailUser(username)

    suspend fun getListFollowing(username: String) =
        RetrofitInstance.apiDetail.getUserFollowing(username)

    suspend fun getListFollowers(username: String) =
        RetrofitInstance.apiDetail.getUserFollowers(username)

    // Local DB
    suspend fun insert(user: FavoriteUsers) = db.getFavUsersDao().insert(user)

    fun getFavUsers() = db.getFavUsersDao().getAllUsers()

    fun getCertainUser(id: Int) = db.getFavUsersDao().getCertainUser(id)

    suspend fun delete(user: FavoriteUsers) = db.getFavUsersDao().deleteUser(user)
}