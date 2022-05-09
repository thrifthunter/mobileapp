package com.thrifthunter.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(private val storiesPreference: StoriesPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return storiesPreference.getStories().asLiveData()
    }

    fun login(token: String) {
        viewModelScope.launch {
            storiesPreference.login(token)
        }
    }
}