package com.ardianeffendi.githubclient.db

import android.net.Uri
import com.ardianeffendi.githubclient.utils.Constants.Companion.TABLE_FAVORITE

class DBContract {

    companion object {
        var CONTENT_AUTHORITY = "com.ardianeffendi.githubuser"
        var CONTENT_URI = Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_FAVORITE)
            .build()
    }
}