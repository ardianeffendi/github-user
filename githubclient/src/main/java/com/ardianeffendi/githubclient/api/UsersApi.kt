package com.ardianeffendi.githubclient.api


import com.ardianeffendi.githubclient.BuildConfig
import com.ardianeffendi.githubclient.models.DetailUserResponse
import com.ardianeffendi.githubclient.models.FollowersResponse
import com.ardianeffendi.githubclient.models.FollowingResponse
import com.ardianeffendi.githubclient.models.Users
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
        @Header("Authorization") token: String = BuildConfig.GITHUB_TOKEN
    ): Response<Users>

    @GET("{username}")
    suspend fun getDetailUser(
        @Path("username")
        username: String,
        @Header("Authorization") token: String = BuildConfig.GITHUB_TOKEN
    ): Response<DetailUserResponse>

    @GET("{username}/followers")
    suspend fun getUserFollowers(
        @Path("username")
        username: String,
        @Header("Authorization") token: String = BuildConfig.GITHUB_TOKEN
    ): Response<FollowersResponse>

    @GET("{username}/following")
    suspend fun getUserFollowing(
        @Path("username")
        username: String,
        @Header("Authorization") token: String = BuildConfig.GITHUB_TOKEN
    ): Response<FollowingResponse>

}