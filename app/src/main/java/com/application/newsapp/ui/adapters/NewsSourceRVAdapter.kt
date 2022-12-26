package com.application.newsapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.newsapp.R
import com.application.newsapp.data.models.Articles
import com.application.newsapp.data.models.Sources
import com.bumptech.glide.Glide

class NewsSourceRVAdapter(
    private val list: List<Sources>,
    val context: Context,
    private val onClickListener: (Sources) -> Unit
) :
    RecyclerView.Adapter<NewsSourceRVAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val sourceText: TextView = itemView.findViewById(R.id.source_text)

        fun bind(source: Sources) {
            sourceText.text = source.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.source_item, parent, false)

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