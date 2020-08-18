package com.ardianeffendi.githubuser.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ardianeffendi.githubuser.models.FavoriteUsers

@Database(
    entities = [FavoriteUsers::class],
    version = 1
)
abstract class FavoriteUsersDatabase : RoomDatabase() {

    abstract fun getFavUsersDao(): FavUsersDao

    companion object {
        @Volatile
        private var instance: FavoriteUsersDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FavoriteUsersDatabase::class.java,
                "favorite_users.db"
            ).build()
    }
}