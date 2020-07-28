package com.ardianeffendi.githubuser.api

import com.ardianeffendi.githubuser.models.DetailUserResponse
import com.ardianeffendi.githubuser.models.FollowersResponse
import com.ardianeffendi.githubuser.models.FollowingResponse
import com.ardianeffendi.githubuser.models.Users
import com.ardianeffendi.githubuser.utils.Constants.Companion.GITHUB_TOKEN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {

    @GET("users")
    suspend fun getUser(
        @Query("q")
        username: String,
        @Header("Authorization") token: String = GITHUB_TOKEN
    ): Response<Users>

    @GET("{username}")
    suspend fun getDetailUser(
        @Path("username")
        username: String,
        @Header("Authorization") token: String = GITHUB_TOKEN
    ): Response<DetailUserResponse>

    @GET("{username}/followers")
    suspend fun getUserFollowers(
        @Path("username")
        username: String,
        @Header("Authorization") token: String = GITHUB_TOKEN
    ): Response<FollowersResponse>

    @GET("{username}/following")
    suspend fun getUserFollowing(
        @Path("username")
        username: String,
        @Header("Authorization") token: String = GITHUB_TOKEN
    ): Response<FollowingResponse>

}