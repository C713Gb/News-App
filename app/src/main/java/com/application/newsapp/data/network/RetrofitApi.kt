package com.application.newsapp.data.network

import com.application.newsapp.data.models.NewsResponse
import com.application.newsapp.data.models.SourcesResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("/v2/top-headlines")
    fun getTopHeadlinesFromSource(
        @Query("sources") sources: String,
        @Query("apiKey") key: String
    ): Observable<NewsResponse>

    @GET("/v2/top-headlines/sources")
    fun getSources(
        @Query("apiKey") key: String
    ): Observable<SourcesResponse>

    @GET("/v2/top-headlines/sources")
    fun getAllSources(
        @Query("apiKey") key: String
    ): Call<SourcesResponse>

}