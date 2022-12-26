package com.application.newsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.application.newsapp.R
import com.application.newsapp.ui.activities.MainActivity
import com.application.newsapp.ui.adapters.NewsSourceRVAdapter
import com.application.newsapp.utils.Constants

class NewsSourcesFragment : Fragment() {

    private lateinit var sourcesRV : RecyclerView
    private lateinit var adapter : NewsSourceRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news_sources, container, false)
        sourcesRV = view.findViewById(R.id.sources_rv)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = requireActivity() as MainActivity

        sourcesRV.setHasFixedSize(true)

        activity.mainViewModel.sources?.observe(activity){ it ->

            adapter = NewsSourceRVAdapter(it.sources, requireActivity()){
                Toast.makeText(requireContext(), "Selected: ${it.name}", Toast.LENGTH_SHORT).show()

                // Save in SharedPreferences
                val sharedPreferences = requireContext().getSharedPreferences(Constants.NEWS_SOURCE, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(Constants.NEWS_ID, it.id)
                editor.putString(Constants.NEWS_TITLE, it.name)
                editor.apply()
                editor.commit()
            }

            sourcesRV.adapter = adapter
        }
    }
}