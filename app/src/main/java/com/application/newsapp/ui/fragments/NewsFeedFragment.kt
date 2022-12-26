package com.application.newsapp.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.application.newsapp.R
import com.application.newsapp.ui.adapters.NewsFeedRVAdapter
import com.application.newsapp.ui.activities.MainActivity
import com.application.newsapp.utils.Constants

class NewsFeedFragment : Fragment() {

    private lateinit var feedRV : RecyclerView
    private lateinit var headerText : TextView
    private lateinit var filter : ImageView
    private lateinit var adapter: NewsFeedRVAdapter
    private lateinit var activity: MainActivity
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news_feed, container, false)
        feedRV = view.findViewById(R.id.recycler)
        headerText = view.findViewById(R.id.header_text)
        filter = view.findViewById(R.id.filter_button)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity = requireActivity() as MainActivity

        sharedPreferences = requireContext().getSharedPreferences(Constants.NEWS_SOURCE, Context.MODE_PRIVATE)
        headerText.text = sharedPreferences.getString(Constants.NEWS_TITLE, "BBC News")

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing =true
            callFeedApi()
        }

        feedRV.setHasFixedSize(true)

        callFeedApi()

        activity.mainViewModel.topHeadlinesFromSource!!.observe(activity){ it ->
            val data = it.articles
            data?.sortedByDescending { it.publishedAt }
            if (data != null) {
                Log.d("TAG", "onViewCreated: ${data.size}")
            }

            adapter = data?.let { it1 ->
                NewsFeedRVAdapter(it1, requireActivity()) {
                    val bundle  = bundleOf("article" to it)
                    findNavController().navigate(R.id.action_newsFeedFragment_to_newsDetailsFragment, bundle)
                }
            }!!
            feedRV.adapter = adapter
        }

        filter.setOnClickListener {
            findNavController().navigate(R.id.action_newsFeedFragment_to_newsSourcesFragment)
        }

    }

    private fun callFeedApi() {
        if (swipeRefreshLayout.isRefreshing)
            swipeRefreshLayout.isRefreshing = false

        val source = sharedPreferences.getString(Constants.NEWS_ID, "bbc-news")
        if (source != null) {
            activity.mainViewModel.getTopHeadlinesFromSource(sources = source, apiKey = Constants.API_KEY)
        }
    }

}