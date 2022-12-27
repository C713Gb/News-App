package com.application.newsapp

import com.application.newsapp.data.models.Sources
import com.application.newsapp.data.models.SourcesResponse
import com.application.newsapp.data.network.RetrofitApi
import com.application.newsapp.utils.Constants
import com.google.gson.Gson
import io.reactivex.observers.TestObserver
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NewsSourceApiTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var retrofitApi: RetrofitApi
    lateinit var testObserver: TestObserver<SourcesResponse>
    lateinit var result: SourcesResponse

    @Before
    fun setup() {
        val source1 = Sources(
            id = "abc-news",
            name = "ABC News",
            description = "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
            url = "https://abcnews.go.com",
            category = "general",
            language = "en",
            country = "us"
        )

        val sources: MutableList<Sources> = ArrayList()
        sources.add(source1)

        result = SourcesResponse(
            status = "ok",
            sources = sources
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
    fun newsSourcesCallWithSuccessful() {
        val mockResponse = MockResponse()
        mockResponse.setBody(Gson().toJson(result))
        mockWebServer.enqueue(mockResponse)

        // When
        runBlocking {
            retrofitApi
                .getSources(Constants.API_KEY)
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