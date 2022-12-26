package com.application.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.application.newsapp.R
import com.application.newsapp.data.models.Articles
import com.bumptech.glide.Glide

class NewsDetailsFragment : Fragment() {

    private lateinit var newsImage : ImageView
    private lateinit var headline : TextView
    private lateinit var description : TextView
    private lateinit var content : TextView
    private lateinit var author : TextView
    private lateinit var published : TextView

    private var article: Articles? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_news_details, container, false)
        newsImage = view.findViewById(R.id.news_img)
        headline = view.findViewById(R.id.headline_text)
        description = view.findViewById(R.id.description_text)
        content = view.findViewById(R.id.content_text)
        author = view.findViewById(R.id.author_text)
        published = view.findViewById(R.id.published_text)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        article = arguments?.getParcelable("article") as Articles?

        if (article != null){
            headline.text = article!!.title
            description.text = "Description:  ${article!!.description}"
            content.text = "Content: ${article!!.content}"
            author.text = "Author: ${article!!.author}"
            published.text = "Published at: ${article!!.publishedAt}"

            Glide
                .with(requireContext())
                .load(article!!.urlToImage)
                .into(newsImage)
        }
    }

}