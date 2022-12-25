package com.application.newsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.newsapp.data.models.NewsResponse
import com.application.newsapp.repository.MainRepository

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    var fetchData: LiveData<NewsResponse>? = null

    fun getData(sources: String, apiKey: String) {
        fetchData = mainRepository.fetchData(sources, apiKey)
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