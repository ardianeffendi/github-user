package com.ardianeffendi.githubuser.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.ardianeffendi.githubuser.db.DBContract
import com.ardianeffendi.githubuser.db.DBContract.Companion.CONTENT_URI
import com.ardianeffendi.githubuser.db.FavoriteUsersDatabase
import com.ardianeffendi.githubuser.models.FavoriteUsers
import com.ardianeffendi.githubuser.utils.Constants.Companion.TABLE_FAVORITE

class MyContentProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        sUriMatcher.addURI(
            DBContract.CONTENT_AUTHORITY, TABLE_FAVORITE, USER
        )
        sUriMatcher.addURI(
            DBContract.CONTENT_AUTHORITY, "$TABLE_FAVORITE/#", USER_ID
        )
    }

    private fun fromContentValues(values: ContentValues): FavoriteUsers {
        return FavoriteUsers(
            values.getAsLong("id").toInt(),
            values.getAsString("username"),
            values.getAsString("name"),
            values.getAsString("avatar")
        )
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val context = context ?: return 0
        val favDao = FavoriteUsersDatabase.invoke(context).getFavUsersDao()
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> favDao.deleteFavoriteCursor(uri.lastPathSegment.toString())
            else -> 0
        }
        context.contentResolver.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val context = context ?: return null
        val favDao = FavoriteUsersDatabase.invoke(context).getFavUsersDao()
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> favDao.insertFavoriteCursor(fromContentValues(values!!))
            else -> 0
        }
        context.contentResolver.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        val context = context ?: return null
        val favDao = FavoriteUsersDatabase.invoke(context).getFavUsersDao()
        cursor = when (sUriMatcher.match(uri)) {
            USER -> favDao.getAllFavoriteCursor()
            USER_ID -> favDao.getFavoritesByIdCursor(uri.lastPathSegment!!.toString())
            else -> null
        }
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val context = context ?: return 0
        val favDao = FavoriteUsersDatabase.invoke(context).getFavUsersDao()
        val updated: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> {
                val favorite = fromContentValues(values!!)
                favorite.id = ContentUris.parseId(uri).toInt()
                favDao.updateFavoriteCursor(favorite)
            }
            else -> 0
        }
        context.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

}
