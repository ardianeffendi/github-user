package com.ardianeffendi.githubuser.db

import android.net.Uri
import com.ardianeffendi.githubuser.BuildConfig
import com.ardianeffendi.githubuser.utils.Constants.Companion.TABLE_FAVORITE

class DBContract {

    companion object {
        var CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID
        var CONTENT_URI = Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_FAVORITE)
            .build()
    }
}