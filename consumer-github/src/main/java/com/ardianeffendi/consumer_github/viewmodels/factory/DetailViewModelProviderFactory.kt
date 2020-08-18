package com.ardianeffendi.consumer_github.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ardianeffendi.consumer_github.repository.UsersRepository
import com.ardianeffendi.consumer_github.viewmodels.DetailViewModel

class DetailViewModelProviderFactory(
    private val usersRepository: UsersRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(usersRepository) as T
    }
}