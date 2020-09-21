package com.ardianeffendi.githubclient.models

data class Users(
    val incomplete_results: Boolean,
    val items: List<UsersItem>,
    val total_count: Int
)