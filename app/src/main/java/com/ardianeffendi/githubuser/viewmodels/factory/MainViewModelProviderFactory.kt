package com.ardianeffendi.githubuser.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ardianeffendi.githubuser.repository.UsersRepository
import com.ardianeffendi.githubuser.viewmodels.MainViewModel

class MainViewModelProviderFactory(
    val app: Application,
    private val usersRepository: UsersRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(app, usersRepository) as T
    }
}