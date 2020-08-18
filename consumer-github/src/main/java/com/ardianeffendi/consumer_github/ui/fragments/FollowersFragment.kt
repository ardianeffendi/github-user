package com.ardianeffendi.consumer_github.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardianeffendi.consumer_github.R
import com.ardianeffendi.consumer_github.adapters.FollowersRecyclerAdapter
import com.ardianeffendi.consumer_github.ui.DetailActivity
import com.ardianeffendi.consumer_github.utils.Resource
import com.ardianeffendi.consumer_github.viewmodels.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    lateinit var viewModel: FollowersViewModel
    lateinit var followersRecyclerAdapter: FollowersRecyclerAdapter

    private val TAG = "FollowersFragment"

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as DetailActivity).followersViewModel

        setupRecyclerView()

        viewModel.listFollowers.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { followersResponse ->
                        followersRecyclerAdapter.differ.submitList(followersResponse)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "There is an error: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        rv_followers.setHasFixedSize(true)
        followersRecyclerAdapter = FollowersRecyclerAdapter()
        followersRecyclerAdapter.notifyDataSetChanged()
        rv_followers.apply {
            adapter = followersRecyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        val username = arguments?.getString(ARG_USERNAME)
        if (username != null) {
            viewModel.getListFollowers(username)
        }

        followersRecyclerAdapter.setOnItemClickListener {
            val moveUserDetail = Intent(activity, DetailActivity::class.java)
            moveUserDetail.putExtra(DetailActivity.EXTRA_DATA, it.login)
            moveUserDetail.putExtra(DetailActivity.EXTRA_ID, it.id)
            startActivity(moveUserDetail)
        }
    }

    private fun showProgressBar() {
        progressBarDetailFollowers.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarDetailFollowers.visibility = View.INVISIBLE
    }


}