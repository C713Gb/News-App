package com.application.newsapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.newsapp.R
import com.application.newsapp.data.models.Articles
import com.bumptech.glide.Glide

class RVAdapter(
    private val list: List<Articles>,
    val context: Context,
    private val onClickListener: (Articles) -> Unit
) :
    RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.news_img)
        private val header: TextView = itemView.findViewById(R.id.headline_text)
        private val date: TextView = itemView.findViewById(R.id.date_text)

        fun bind(article: Articles) {
            header.text = article.title
            date.text = article.publishedAt

            Glide.with(itemView.context)
                .load(article.urlToImage)
                .into(imageView)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            onClickListener(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}