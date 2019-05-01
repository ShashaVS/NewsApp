package com.android.shashavs.guardianclient.utils

import android.util.Log
import java.lang.NullPointerException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

    companion object {
        private val TAG = "DateTimeUtil"
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

        fun getDate(webPublicationDate: String?): Date? {
            try {
                return dateFormat.parse(webPublicationDate)
            } catch (e: ParseException) {
                Log.e(TAG, "getDate ParseException", e)
            } catch (e: NullPointerException) {
                Log.e(TAG, "getDate NullPointerException", e)
            }
            return null
        }
    }

}