package com.thrifthunter.paging

import com.thrifthunter.ApiConfig

object Injection {
    fun provideRepository(token: String) : StoriesRepository{
        val apiService = ApiConfig().getApiService()
        return StoriesRepository(apiService, token)
    }
}