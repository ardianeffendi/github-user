package com.ardianeffendi.githubclient.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ardianeffendi.githubclient.repository.UsersRepository
import com.ardianeffendi.githubclient.viewmodels.FollowingViewModel

class FollowingViewModelProviderFactory(
    private val usersRepository: UsersRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowingViewModel(
            usersRepository
        ) as T
    }
}