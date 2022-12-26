package com.application.newsapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SourcesResponse(
    val status: String,
    val sources: List<Sources>
) : Parcelable

@Parcelize
data class Sources(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
) : Parcelable
