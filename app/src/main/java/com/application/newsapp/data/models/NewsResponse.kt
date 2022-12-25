package com.application.newsapp.data.models

data class NewsResponse(
    val status: String,
    val totalResults: String,
    val articles: List<Articles>
)

data class Articles(
    val source:  ArticleSource,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)

data class ArticleSource(
    val id: String,
    val name: String
)
