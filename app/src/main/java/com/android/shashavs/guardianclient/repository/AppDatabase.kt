package com.android.shashavs.guardianclient.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.android.shashavs.guardianclient.repository.dao.NewsDao
import com.android.shashavs.guardianclient.repository.entity.NewsEntity

@Database(entities = arrayOf(NewsEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}