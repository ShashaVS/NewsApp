package com.android.shashavs.guardianclient.repository.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(@PrimaryKey val id: String,
                      val type: String,
                      val sectionId: String,
                      val sectionName: String,
                      val webPublicationDate: String,
                      val webTitle: String,
                      val webUrl: String,
                      val apiUrl: String,
                      val isHosted: Boolean,
                      val pillarId: String,
                      val pillarName: String,
                      val thumbnail: String)