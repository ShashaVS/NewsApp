package com.android.shashavs.guardianclient.repository.room.dao

import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.android.shashavs.guardianclient.repository.data_objects.News

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

    @Query("SELECT * FROM news WHERE id = :id LIMIT 1")
    fun description(id: String): List<News>
}