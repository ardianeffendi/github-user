package com.ardianeffendi.githubuser.repository

import com.ardianeffendi.githubuser.api.RetrofitInstance

class UsersRepository {

    suspend fun getUser(username: String) = RetrofitInstance.api.getUser(username)
    suspend fun getDetailUser(username: String) = RetrofitInstance.apiDetail.getDetailUser(username)
    suspend fun getListFollowing(username: String) =
        RetrofitInstance.apiDetail.getUserFollowing(username)

    suspend fun getListFollowers(username: String) =
        RetrofitInstance.apiDetail.getUserFollowers(username)
}