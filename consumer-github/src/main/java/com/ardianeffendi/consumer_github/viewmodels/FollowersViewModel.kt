package com.ardianeffendi.consumer_github.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardianeffendi.consumer_github.models.FollowersResponse
import com.ardianeffendi.consumer_github.repository.UsersRepository
import com.ardianeffendi.consumer_github.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class FollowersViewModel(
    val usersRepository: UsersRepository
) : ViewModel() {

    val listFollowers: MutableLiveData<Resource<FollowersResponse>> = MutableLiveData()

    fun getListFollowers(username: String) = viewModelScope.launch {
        listFollowers.postValue(Resource.Loading())
        val getFollowersResponse = usersRepository.getListFollowers(username)
        listFollowers.postValue(handlingGetFollowingResponse(getFollowersResponse))
    }

    private fun handlingGetFollowingResponse(response: Response<FollowersResponse>): Resource<FollowersResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}