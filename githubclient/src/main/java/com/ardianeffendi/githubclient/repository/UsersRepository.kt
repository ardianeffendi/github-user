package com.ardianeffendi.githubclient.repository

import com.ardianeffendi.githubclient.api.RetrofitInstance

class UsersRepository() {

    // Network Call
    suspend fun getUser(username: String) = RetrofitInstance.api.getUser(username)

    suspend fun getDetailUser(username: String) = RetrofitInstance.apiDetail.getDetailUser(username)

    suspend fun getListFollowing(username: String) =
        RetrofitInstance.apiDetail.getUserFollowing(username)

    suspend fun getListFollowers(username: String) =
        RetrofitInstance.apiDetail.getUserFollowers(username)

}