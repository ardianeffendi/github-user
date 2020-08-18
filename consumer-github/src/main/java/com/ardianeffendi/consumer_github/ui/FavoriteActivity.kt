package com.ardianeffendi.consumer_github.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ardianeffendi.consumer_github.R
import com.ardianeffendi.consumer_github.adapters.FavoriteRecyclerAdapter
import com.ardianeffendi.consumer_github.adapters.MainRecyclerAdapter
import com.ardianeffendi.consumer_github.db.FavoriteUsersDatabase
import com.ardianeffendi.consumer_github.models.UsersItem
import com.ardianeffendi.consumer_github.repository.UsersRepository
import com.ardianeffendi.consumer_github.viewmodels.DetailViewModel
import com.ardianeffendi.consumer_github.viewmodels.factory.DetailViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*

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
        viewModel = ViewModelProvider(this, favoriteViewModelFactory).get(DetailViewModel::class.java)

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
                Snackbar.make(constraint_layout_favorite, "User has been removed!", Snackbar.LENGTH_LONG).apply {
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

        viewModel.getFavUsers().observe(this, Observer { users ->
            favoriteRecyclerAdapter.differ.submitList(users)
        })

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
}

