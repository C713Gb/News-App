package com.application.newsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.newsapp.data.models.NewsResponse
import com.application.newsapp.data.models.SourcesResponse
import com.application.newsapp.repository.MainRepository

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    var topHeadlinesFromSource: LiveData<NewsResponse>? = null
    var sources: LiveData<SourcesResponse>? = null

    fun getTopHeadlinesFromSource(sources: String, apiKey: String) {
        topHeadlinesFromSource = mainRepository.fetchTopHeadlinesFromSource(sources, apiKey)
    }

    fun getSources(apiKey: String){
        sources = mainRepository.fetchSources(apiKey)
    }

}

class MainViewModelFactory(private val mainRepository: MainRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }

}