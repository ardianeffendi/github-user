package com.ardianeffendi.githubuser.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ardianeffendi.githubuser.R
import com.ardianeffendi.githubuser.adapters.SectionsPagerAdapter
import com.ardianeffendi.githubuser.models.UsersItem
import com.ardianeffendi.githubuser.repository.UsersRepository
import com.ardianeffendi.githubuser.utils.Resource
import com.ardianeffendi.githubuser.viewmodels.DetailViewModel
import com.ardianeffendi.githubuser.viewmodels.FollowersViewModel
import com.ardianeffendi.githubuser.viewmodels.FollowingViewModel
import com.ardianeffendi.githubuser.viewmodels.factory.DetailViewModelProviderFactory
import com.ardianeffendi.githubuser.viewmodels.factory.FollowersViewModelProviderFactory
import com.ardianeffendi.githubuser.viewmodels.factory.FollowingViewModelProviderFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var detailViewModel: DetailViewModel
    lateinit var followersViewModel: FollowersViewModel
    lateinit var followingViewModel: FollowingViewModel

    private val detailTitle = "Profile"
    private val TAG = "DetailActivity"

    companion object {
        const val EXTRA_DATA = "extra_data"
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

        val repo = UsersRepository()

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

        val userDetail = intent.getParcelableExtra(EXTRA_DATA) as UsersItem
        setupProfileHeader(userDetail.login)
        sectionsPagerAdapter.username = userDetail.login

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
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
            }
        })

    }

    private fun setupProfileHeader(username: String) {
        detailViewModel.getDetail(username)
    }

}