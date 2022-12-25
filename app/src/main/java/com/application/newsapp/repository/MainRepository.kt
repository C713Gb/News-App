package com.application.newsapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.application.newsapp.data.models.NewsResponse
import com.application.newsapp.data.network.RetrofitClient
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainRepository {

    private val retrofit = RetrofitClient.create()
    lateinit var disposable: Disposable

    fun fetchData(sources: String, apiKey: String): MutableLiveData<NewsResponse> {

        val list = MutableLiveData<NewsResponse>()

        retrofit.getBBCTopHeadlines(sources, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (object : Observer<NewsResponse>{
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: NewsResponse) {
                    list.postValue(t)
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "onError: ${e.message}")
                }

                override fun onComplete() {

                }
            })


        return list
    }

}