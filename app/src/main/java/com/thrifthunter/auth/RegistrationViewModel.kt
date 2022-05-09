package com.thrifthunter.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegistrationViewModel(private val storiesPreference: StoriesPreference) : ViewModel() {

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            storiesPreference.saveUser(user)
        }
    }
}