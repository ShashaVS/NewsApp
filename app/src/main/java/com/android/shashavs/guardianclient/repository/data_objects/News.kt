package com.android.shashavs.guardianclient.repository.data_objects

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "news")
data class News(@PrimaryKey val id: String,
                var currentPage: Int?,
                val type: String?,
                val sectionId: String?,
                val sectionName: String?,
                val webPublicationDate: String?,
                val webTitle: String?,
                val webUrl: String?,
                val apiUrl: String?,
                @Embedded val fields: Fields?) : Serializable

data class Fields(val trailText: String?,
                  val headline: String?,
                  var body: String?,
                  val score: Float?,
                  val thumbnail: String?,
                  val internalPageCode: String?,
                  val productionOffice: String?,
                  val starRating: Int?)