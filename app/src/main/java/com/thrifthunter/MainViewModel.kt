package com.thrifthunter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.thrifthunter.settings.ListStory
import com.thrifthunter.auth.StoriesPreference
import com.thrifthunter.auth.UserModel
import com.thrifthunter.paging.StoriesRepository
import kotlinx.coroutines.launch

class MainViewModel(private val storiesPreference: StoriesPreference, storiesRepository: StoriesRepository) :
    ViewModel() {
    fun getStories(): LiveData<UserModel> {
        return storiesPreference.getStories().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            storiesPreference.logout()
        }
    }

    val story : LiveData<PagingData<ListStory>> = storiesRepository.getStories().cachedIn(viewModelScope)
}