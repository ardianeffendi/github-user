package com.ardianeffendi.githubuser.ui

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ardianeffendi.githubuser.R
import com.ardianeffendi.githubuser.adapters.SectionsPagerAdapter
import com.ardianeffendi.githubuser.db.DBContract.Companion.CONTENT_URI
import com.ardianeffendi.githubuser.db.FavoriteUsersDatabase
import com.ardianeffendi.githubuser.helper.MappingHelper
import com.ardianeffendi.githubuser.models.FavoriteUsers
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    lateinit var detailViewModel: DetailViewModel
    lateinit var followersViewModel: FollowersViewModel
    lateinit var followingViewModel: FollowingViewModel

    private val detailTitle = "Profile"
    private lateinit var uriWithId: Uri
    private var isFavoriteUsers: Boolean = false
    private var favoriteUsers: FavoriteUsers? = null

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
        val userID = intent.getIntExtra(EXTRA_ID, 0)
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
        // TODO: check this function's parameter!
        checkUserExists(userID)

        fab.setOnClickListener {
            detailViewModel.detailUser.observe(this, Observer { user ->
                when (user) {
                    is Resource.Success -> {
                        user.data?.let {
                            GlobalScope.launch(Dispatchers.Main) {
                                async(Dispatchers.IO) {
                                    if (isFavoriteUsers) {
                                        contentResolver.delete(uriWithId, null, null)
                                        Snackbar.make(
                                            constraint_layout_detail,
                                            "User removed from favorite!",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                        isFavoriteUsers = false
                                        setStatusFavorite()
                                    } else {
                                        val values = ContentValues()
                                        values.put("id", it.id)
                                        values.put("username", it.login)
                                        values.put("name", it.name)
                                        values.put("avatar", it.avatar_url)
                                        contentResolver.insert(CONTENT_URI, values)
                                        Snackbar.make(
                                            constraint_layout_detail,
                                            "User added to favorite!",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                        isFavoriteUsers = true
                                        setStatusFavorite()
                                    }
                                }
                            }
                        }
                    }
                }
            })


            // TODO: LiveData Implementation of get Favorite Users
//            detailViewModel.detailUser.observe(this, Observer { response ->
//                when (response) {
//                    is Resource.Success -> {
//                        response.data?.let { detailUserResponse ->
//                            favoriteUsers = FavoriteUsers(
//                                detailUserResponse.id,
//                                detailUserResponse.login,
//                                detailUserResponse.name,
//                                detailUserResponse.avatar_url
//                            )
//                            if (!isFavoriteUsers) {
//                                detailViewModel.saveUser(favoriteUsers!!)
//                                setStatusFavorite()
//                                Snackbar.make(
//                                    constraint_layout_detail,
//                                    "User added as favorite!",
//                                    Snackbar.LENGTH_SHORT
//                                ).show()
//                            } else {
//                                detailViewModel.deleteUser(favoriteUsers!!)
//                                setStatusFavorite()
//                                Snackbar.make(
//                                    constraint_layout_detail,
//                                    "User removed from favorite!",
//                                    Snackbar.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    }
//                    is Resource.Error -> {
//                        response.message?.let { message ->
//                            Toast.makeText(
//                                this,
//                                "Cannot retrieve user!, $message",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                }
//            })
        }

    }

    private fun checkUserExists(id: Int) {
        // TODO: Checking User exists with LiveData
//        detailViewModel.getCertainUser(id).observe(this, Observer { currentID ->
//            if (currentID.isNotEmpty()) {
//                val userExists = currentID[0].id == id
//                if (userExists) {
//                    isFavoriteUsers = true
//                    setStatusFavorite()
//                    uriWithId = Uri.parse()
//                } else {
//                    isFavoriteUsers = false
//                    setStatusFavorite()
//
//                }
//            }
//        })
        uriWithId = Uri.parse("$CONTENT_URI/$id")
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(uriWithId, null, null, null, null)
                MappingHelper.mapCursorToObject(cursor)
            }
            favoriteUsers = deferredFavorites.await()
            if (favoriteUsers?.id == id) {
                isFavoriteUsers = true
                setStatusFavorite()
            } else {
                isFavoriteUsers = false
                setStatusFavorite()
            }
        }
    }

    private fun setStatusFavorite() {
        if (isFavoriteUsers) {
            fab.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun setupProfile(username: String) {
        detailViewModel.getDetail(username)
    }

}