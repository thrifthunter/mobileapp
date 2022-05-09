package com.thrifthunter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thrifthunter.auth.LoginViewModel
import com.thrifthunter.auth.RegistrationViewModel
import com.thrifthunter.auth.StoriesPreference
import com.thrifthunter.paging.Injection

class ViewModelFactory(private val storiesPreference: StoriesPreference, private val token: String) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storiesPreference, Injection.provideRepository(token)) as T
            }
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) -> {
                RegistrationViewModel(storiesPreference) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(storiesPreference) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}