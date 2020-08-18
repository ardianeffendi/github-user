package com.ardianeffendi.githubuser.ui

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ardianeffendi.githubuser.R
import com.ardianeffendi.githubuser.adapters.FavoriteRecyclerAdapter
import com.ardianeffendi.githubuser.db.DBContract.Companion.CONTENT_URI
import com.ardianeffendi.githubuser.db.FavoriteUsersDatabase
import com.ardianeffendi.githubuser.helper.MappingHelper
import com.ardianeffendi.githubuser.repository.UsersRepository
import com.ardianeffendi.githubuser.viewmodels.DetailViewModel
import com.ardianeffendi.githubuser.viewmodels.factory.DetailViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    lateinit var viewModel: DetailViewModel
    lateinit var favoriteRecyclerAdapter: FavoriteRecyclerAdapter

    private val titleFavorite = "Favorites"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        // Back Button on Action Bar
        val actionBar = supportActionBar
        actionBar?.title = titleFavorite
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val repo = UsersRepository(FavoriteUsersDatabase(this))
        val favoriteViewModelFactory = DetailViewModelProviderFactory(repo)
        viewModel =
            ViewModelProvider(this, favoriteViewModelFactory).get(DetailViewModel::class.java)

        setupRecyclerView()

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
                viewModel.deleteUser(user)
                Snackbar.make(
                    constraint_layout_favorite,
                    "User has been removed!",
                    Snackbar.LENGTH_LONG
                ).apply {
                    setAction("Undo") {
                        viewModel.saveUser(user)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rv_favorite)
        }

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

//        viewModel.getFavUsers().observe(this, Observer { users ->
//            favoriteRecyclerAdapter.differ.submitList(users)
//        })

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
//            showProgressBar()
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFavorites.await()
//            hideProgressBar()
            if (favorites.size > 0) {
                // TODO: Delete LOG
                Log.d("loadFavoriteAsync", "Problem ----> $favorites")
                favoriteRecyclerAdapter.differ.submitList(favorites)
            } else {
                favoriteRecyclerAdapter.differ.submitList(ArrayList())
                Snackbar.make(rv_favorite, "No data to be shown!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

//    private fun showProgressBar() {
//        progressBar.visibility = View.VISIBLE
//    }
//
//    private fun hideProgressBar() {
//        progressBar.visibility = View.INVISIBLE
//    }

}

