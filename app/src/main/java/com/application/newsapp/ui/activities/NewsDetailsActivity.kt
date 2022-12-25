package com.application.newsapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.application.newsapp.R
import com.bumptech.glide.Glide

class NewsDetailsActivity : AppCompatActivity() {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_news_details)

        val imageView = findViewById<ImageView>(R.id.news_img)
        val headline = findViewById<TextView>(R.id.headline_text)
        val author = findViewById<TextView>(R.id.author_text)
        val published = findViewById<TextView>(R.id.published_text)
        val description = findViewById<TextView>(R.id.description_text)
        val content = findViewById<TextView>(R.id.content_text)

        if (intent != null){

            headline.text = intent.getStringExtra("headline")
            author.text = "Author: ${intent.getStringExtra("author")}"
            published.text = "Published at: ${intent.getStringExtra("published")}"
            description.text = "Description: ${intent.getStringExtra("description")}"
            content.text = "Content: ${intent.getStringExtra("content")}"

            val image = intent.getStringExtra("image")

            Glide
                .with(this@NewsDetailsActivity)
                .load(image)
                .into(imageView)

        }

    }
}