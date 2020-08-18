package com.ardianeffendi.consumer_github.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ardianeffendi.consumer_github.R
import com.ardianeffendi.consumer_github.adapters.SectionsPagerAdapter
import com.ardianeffendi.consumer_github.db.FavoriteUsersDatabase
import com.ardianeffendi.consumer_github.models.FavoriteUsers
import com.ardianeffendi.consumer_github.repository.UsersRepository
import com.ardianeffendi.consumer_github.utils.Resource
import com.ardianeffendi.consumer_github.viewmodels.DetailViewModel
import com.ardianeffendi.consumer_github.viewmodels.FollowersViewModel
import com.ardianeffendi.consumer_github.viewmodels.FollowingViewModel
import com.ardianeffendi.consumer_github.viewmodels.factory.DetailViewModelProviderFactory
import com.ardianeffendi.consumer_github.viewmodels.factory.FollowersViewModelProviderFactory
import com.ardianeffendi.consumer_github.viewmodels.factory.FollowingViewModelProviderFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var detailViewModel: DetailViewModel
    lateinit var followersViewModel: FollowersViewModel
    lateinit var followingViewModel: FollowingViewModel

    private val detailTitle = "Profile"

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Back Button on Action Bar
        val actionBar = supportActionBar

        actionBar?.title = detailTitle
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )

        view_pager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(view_pager)

        val repo = UsersRepository(FavoriteUsersDatabase(this))

        val detailViewModelFactory = DetailViewModelProviderFactory(repo)
        detailViewModel =
            ViewModelProvider(this, detailViewModelFactory).get(DetailViewModel::class.java)

        val followersViewModelProviderFactory =
            FollowersViewModelProviderFactory(repo)
        followersViewModel = ViewModelProvider(this, followersViewModelProviderFactory).get(
            FollowersViewModel::class.java
        )

        val followingViewModelProviderFactory =
            FollowingViewModelProviderFactory(repo)
        followingViewModel = ViewModelProvider(this, followingViewModelProviderFactory).get(
            FollowingViewModel::class.java
        )

        val userDetail = intent.getStringExtra(EXTRA_DATA) as String
        val userID = intent.getIntExtra(EXTRA_ID, 0) as Int
        setupProfile(userDetail)
        sectionsPagerAdapter.username = userDetail

        detailViewModel.detailUser.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { userResponse ->
                        Glide.with(this)
                            .load(userResponse.avatar_url)
                            .apply(RequestOptions().override(120, 120))
                            .into(circleImageView)
                        profile_name_detail.text = userResponse.name
                        user_name_detail.text = userResponse.login
                        company_detail.text = userResponse.company
                        location_detail.text = userResponse.location
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(this, "Cannot retrieve user!, $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })

        // check if current user has been added to favorite
        checkUserExists(userID)

        fab.setOnClickListener {
            detailViewModel.detailUser.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { detailUserResponse ->
                            val favorite = FavoriteUsers(
                                detailUserResponse.id,
                                detailUserResponse.login,
                                detailUserResponse.name,
                                detailUserResponse.avatar_url
                            )
                            detailViewModel.saveUser(favorite)
                            Snackbar.make(
                                constraint_layout_detail,
                                "User added as favorite!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is Resource.Error -> {
                        response.message?.let { message ->
                            Toast.makeText(
                                this,
                                "Cannot retrieve user!, $message",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
        }

    }

    private fun checkUserExists(id: Int) {
        detailViewModel.getCertainUser(id).observe(this, Observer { currentID ->
            if (currentID.isNotEmpty()) {
                val userExists = currentID[0].id == id
                if (userExists) {
                    fab.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }
        })
    }


    private fun setupProfile(username: String) {
        detailViewModel.getDetail(username)
    }

}