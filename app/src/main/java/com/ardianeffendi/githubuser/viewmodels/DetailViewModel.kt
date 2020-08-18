package com.ardianeffendi.githubuser.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardianeffendi.githubuser.models.DetailUserResponse
import com.ardianeffendi.githubuser.repository.UsersRepository
import com.ardianeffendi.githubuser.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    val detailUser: MutableLiveData<Resource<DetailUserResponse>> = MutableLiveData()

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

}