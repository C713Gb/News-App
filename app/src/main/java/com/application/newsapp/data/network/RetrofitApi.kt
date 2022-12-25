package com.application.newsapp.data.network

import androidx.lifecycle.MutableLiveData
import com.application.newsapp.data.models.NewsResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("/v2/top-headlines")
    fun getBBCTopHeadlines(
        @Query("sources") sources: String,
        @Query("apiKey") key: String
    ): Observable<NewsResponse>

    @GET("/v2/top-headlines")
    fun getBBTopHeadlines(
        @Query("sources") sources: String,
        @Query("apiKey") key: String
    ): Call<NewsResponse>

}