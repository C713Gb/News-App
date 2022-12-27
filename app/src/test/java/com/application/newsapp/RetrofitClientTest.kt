package com.application.newsapp

import com.application.newsapp.data.models.SourcesResponse
import com.application.newsapp.data.network.RetrofitClient
import com.application.newsapp.utils.Constants
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

class RetrofitClientTest {

    lateinit var testObserver: TestObserver<SourcesResponse>

    @Before
    fun setup(){
        testObserver = TestObserver()
    }

    @Test
    fun testRetrofitInstance() {
        val api = RetrofitClient.create()
        val result = api.getAllSources(Constants.API_KEY).execute()

        val errorBody = result.errorBody()
        assert(errorBody == null)
        //Check for success body
        val responseWrapper = result.body()
        assert(responseWrapper != null)
        assert(result.code() == 200)
    }
}