package com.application.newsapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(
    val status: String? = null,
    val totalResults: String? = null,
    val articles: List<Articles>? = null
): Parcelable

@Parcelize
data class Articles(
    val source:  ArticleSource? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
) : Parcelable

@Parcelize
data class ArticleSource(
    val id: String? = null,
    val name: String? = null
) : Parcelable
