package com.ardianeffendi.githubuser.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ardianeffendi.githubuser.repository.UsersRepository
import com.ardianeffendi.githubuser.viewmodels.FollowingViewModel

class FollowingViewModelProviderFactory(
    private val usersRepository: UsersRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowingViewModel(
            usersRepository
        ) as T
    }
}