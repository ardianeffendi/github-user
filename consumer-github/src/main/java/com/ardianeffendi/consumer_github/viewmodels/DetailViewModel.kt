package com.ardianeffendi.consumer_github.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardianeffendi.consumer_github.models.DetailUserResponse
import com.ardianeffendi.consumer_github.models.FavoriteUsers
import com.ardianeffendi.consumer_github.repository.UsersRepository
import com.ardianeffendi.consumer_github.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    val detailUser: MutableLiveData<Resource<DetailUserResponse>> = MutableLiveData()
    val favoriteUsers: MutableLiveData<Resource<FavoriteUsers>> = MutableLiveData()

    fun getDetail(username: String) = viewModelScope.launch {
        detailUser.postValue(Resource.Loading())
        val response = usersRepository.getDetailUser(username)
        detailUser.postValue(handleUsersResponse(response))
    }

    private fun handleUsersResponse(response: Response<DetailUserResponse>): Resource<DetailUserResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveUser(user: FavoriteUsers) = viewModelScope.launch {
        usersRepository.insert(user)
    }

    fun deleteUser(user: FavoriteUsers) = viewModelScope.launch {
        usersRepository.delete(user)
    }

    fun getFavUsers() = usersRepository.getFavUsers()

    fun getCertainUser(id: Int) = usersRepository.getCertainUser(id)



}