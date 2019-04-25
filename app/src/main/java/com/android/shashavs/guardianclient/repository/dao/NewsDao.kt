package com.android.shashavs.guardianclient.repository.dao

import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.android.shashavs.guardianclient.retrofit.objects.News

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER BY webPublicationDate DESC")
    fun getNews(): DataSource.Factory<Int, News>

    @Query("UPDATE news SET body=:desc WHERE id = :id")
    fun addDesc(id: String, desc: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(newsList : List<News>) : List<Long>

    @Update
    fun updateNews(news: News) : Int

    @Query("SELECT * FROM news WHERE id = :id")
    fun description(id: String): News
}