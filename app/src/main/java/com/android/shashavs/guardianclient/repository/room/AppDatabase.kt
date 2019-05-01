package com.android.shashavs.guardianclient.repository.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.android.shashavs.guardianclient.repository.room.dao.NewsDao
import com.android.shashavs.guardianclient.repository.data_objects.News

@Database(entities = arrayOf(News::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}