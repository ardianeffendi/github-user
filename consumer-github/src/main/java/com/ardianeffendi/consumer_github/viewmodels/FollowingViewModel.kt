package com.ardianeffendi.consumer_github.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardianeffendi.consumer_github.models.FollowingResponse
import com.ardianeffendi.consumer_github.repository.UsersRepository
import com.ardianeffendi.consumer_github.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class FollowingViewModel(
    val usersRepository: UsersRepository
) : ViewModel() {

    val listFollowing: MutableLiveData<Resource<FollowingResponse>> = MutableLiveData()

    fun getListFollowing(username: String) = viewModelScope.launch {
        listFollowing.postValue(Resource.Loading())
        val getFollowingResponse = usersRepository.getListFollowing(username)
        listFollowing.postValue(handlingGetFollowingResponse(getFollowingResponse))
    }

    private fun handlingGetFollowingResponse(response: Response<FollowingResponse>): Resource<FollowingResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}