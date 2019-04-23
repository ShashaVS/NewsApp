package com.android.shashavs.guardianclient.repository.dao

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.android.shashavs.guardianclient.retrofit.objects.News

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER BY webPublicationDate DESC")
    fun getNews(): DataSource.Factory<Int, News>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(newsList : List<News>) : List<Long>
}