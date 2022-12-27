package com.application.newsapp

import com.application.newsapp.data.models.ArticleSource
import com.application.newsapp.data.models.Articles
import com.application.newsapp.data.models.NewsResponse
import com.application.newsapp.data.network.RetrofitApi
import com.application.newsapp.utils.Constants
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NewsFeedApiTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var retrofitApi: RetrofitApi
    lateinit var testObserver: TestObserver<NewsResponse>
    lateinit var result: NewsResponse

    @Before
    fun setup(){
        val articles: MutableList<Articles> = ArrayList()
        val article1 = Articles(
            ArticleSource(
                id = "bbc-news",
                name = "BBC News"
            ),
            author = "BBC Sport",
            title = "Premier League: Van Dijk doubles Liverpool lead at Villa",
            description = "Follow all the action as the Premier League returns after the World Cup with seven matches.",
            url = "http://www.bbc.co.uk/sport/live/football/62619649",
            urlToImage = "https:////m.files.bbci.co.uk/modules/bbc-morph-sport-seo-meta/1.23.3/images/bbc-sport-logo.png",
            publishedAt = "2022-12-26T18:22:33.1388763Z",
            content = "Lead. Doubled.\r\nAston Villa fail to clear a corner and Mohamed Salah keeps it alive, popping the ball off for Virgil van Dijk to rasp a first time, left-footed finish low into the back of the net.\r\nT… [+138 chars]"
        )

        articles.add(article1)

        result = NewsResponse(
            status = "ok",
            totalResults = "10",
            articles = articles
        )

        mockWebServer = MockWebServer()
        retrofitApi  =  Retrofit.Builder()
            .baseUrl(mockWebServer.url(Constants.API_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(RetrofitApi::class.java)

        testObserver = TestObserver()
    }

    @Test
    fun newsFeedCallWithSuccessful() {
        val mockResponse = MockResponse()
        mockResponse.setBody(Gson().toJson(result))
        mockWebServer.enqueue(mockResponse)

        // When
        runBlocking {
            retrofitApi
                .getTopHeadlinesFromSource("bbc-news", Constants.API_KEY)
                .subscribe(testObserver)
        }

        // Then
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @After
    fun shutDown(){
        mockWebServer.shutdown()
    }

}