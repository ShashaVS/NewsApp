package com.android.shashavs.guardianclient.repository.retrofit

import com.android.shashavs.guardianclient.repository.retrofit.objects.News
import com.android.shashavs.guardianclient.repository.retrofit.objects.PageResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("search")
    fun getNewsList(@Query("page") page : Int? = null,
                    @Query("q") query: String? = null,
                    @Query("show-fields") showFields : String? = null,
                    @Query("api-key") apiKey : String) : Observable<Response<PageResponse<News>>>

    @GET("{id}")
    fun getNews(@Path("id", encoded = true) id: String,
                @Query("show-fields") showFields : String? = null,
                @Query("api-key") apiKey : String) : Observable<Response<PageResponse<News>>>

}