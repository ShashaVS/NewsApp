package com.android.shashavs.guardianclient.repository.data_objects

data class PageResponse<T>(val response: PaginationData<T>)

data class PaginationData<T>(val status: String,
                             val userTier: String,
                             val total: Int,
                             val startIndex: Int,
                             val pageSize: Int,
                             val currentPage: Int,
                             val pages: Int,
                             val orderBy: String,
                             val results: List<T>?,
                             val content: T)