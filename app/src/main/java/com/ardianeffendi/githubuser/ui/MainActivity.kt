package com.ardianeffendi.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardianeffendi.githubuser.R
import com.ardianeffendi.githubuser.adapters.MainRecyclerAdapter
import com.ardianeffendi.githubuser.repository.UsersRepository
import com.ardianeffendi.githubuser.utils.Resource
import com.ardianeffendi.githubuser.viewmodels.MainViewModel
import com.ardianeffendi.githubuser.viewmodels.factory.MainViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var mainRecyclerAdapter: MainRecyclerAdapter

    private val TAG = "MainActivity"
    private val titleMain = "GitHub Users"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = titleMain

        val repository = UsersRepository()
        val viewModelProviderFactory = MainViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)

        setupRecyclerView()

        viewModel.queryUser.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { userResponse ->
                        if (userResponse.items.isEmpty()) {
                            Toast.makeText(this, "Failed to find a user!", Toast.LENGTH_LONG).show()
                        } else {
                            mainRecyclerAdapter.differ.submitList(userResponse.items)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun setupRecyclerView() {
        rv_users.setHasFixedSize(true)
        mainRecyclerAdapter = MainRecyclerAdapter()
        mainRecyclerAdapter.notifyDataSetChanged()
        rv_users.apply {
            adapter = mainRecyclerAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        mainRecyclerAdapter.setOnItemClickListener {
            val moveUserDetail = Intent(this@MainActivity, DetailActivity::class.java)
            moveUserDetail.putExtra(DetailActivity.EXTRA_DATA, it)
            startActivity(moveUserDetail)
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }
}