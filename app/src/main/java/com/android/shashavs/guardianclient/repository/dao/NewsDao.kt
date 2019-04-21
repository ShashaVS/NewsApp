package com.android.shashavs.guardianclient.repository.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.android.shashavs.guardianclient.repository.entity.NewsEntity

@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getNews(): List<NewsEntity>

    @Insert
    fun insert(newsList : List<NewsEntity>)
}