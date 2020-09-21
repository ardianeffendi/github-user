package com.ardianeffendi.githubuser.ui

import android.content.ContentValues
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ardianeffendi.githubuser.R
import com.ardianeffendi.githubuser.adapters.FavoriteRecyclerAdapter
import com.ardianeffendi.githubuser.db.DBContract.Companion.CONTENT_URI
import com.ardianeffendi.githubuser.helper.MappingHelper
import com.ardianeffendi.githubuser.models.FavoriteUsers
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    lateinit var favoriteRecyclerAdapter: FavoriteRecyclerAdapter

    private val titleFavorite = "Favorites"
    private var favoriteUsers: FavoriteUsers? = null
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        // Back Button on Action Bar
        val actionBar = supportActionBar
        actionBar?.title = titleFavorite
        actionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadFavoritesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavoritesAsync()
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val user = favoriteRecyclerAdapter.differ.currentList[position]
                uriWithId = Uri.parse("$CONTENT_URI/${user.id}")
                favoriteUsers =
                    FavoriteUsers(user.id, user.login, user.name, user.avatar_url)
                GlobalScope.launch {
                    contentResolver.delete(uriWithId, null, null)
                    Snackbar.make(
                        constraint_layout_favorite,
                        "User has been removed!",
                        Snackbar.LENGTH_LONG
                    ).apply {
                        setAction("Undo") {
                            val values = ContentValues()
                            values.put("id", favoriteUsers!!.id)
                            values.put("username", favoriteUsers!!.login)
                            values.put("name", favoriteUsers!!.name)
                            values.put("avatar", favoriteUsers!!.avatar_url)
                            contentResolver.insert(CONTENT_URI, values)
                        }
                        show()
                    }
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rv_favorite)
        }

    }

    private fun setupRecyclerView() {
        favoriteRecyclerAdapter = FavoriteRecyclerAdapter()
        rv_favorite.apply {
            adapter = favoriteRecyclerAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }
        favoriteRecyclerAdapter.setOnItemClickListener {
            val moveUserDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
            moveUserDetail.putExtra(DetailActivity.EXTRA_DATA, it.login)
            moveUserDetail.putExtra(DetailActivity.EXTRA_ID, it.id)
            startActivity(moveUserDetail)
        }
    }

    fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                favoriteRecyclerAdapter.differ.submitList(favorites)
            } else {
                favoriteRecyclerAdapter.differ.submitList(ArrayList())
                Snackbar.make(rv_favorite, "No data to be shown!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}

