package com.ardianeffendi.githubuser.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardianeffendi.githubuser.R
import com.ardianeffendi.githubuser.adapters.FollowingRecyclerAdapter
import com.ardianeffendi.githubuser.ui.DetailActivity
import com.ardianeffendi.githubuser.utils.Resource
import com.ardianeffendi.githubuser.viewmodels.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment(R.layout.fragment_following) {

    lateinit var viewModel: FollowingViewModel
    lateinit var followingRecyclerAdapter: FollowingRecyclerAdapter

    private val TAG = "FollowingFragment"

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as DetailActivity).followingViewModel
        setupRecyclerView()

        viewModel.listFollowing.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { followingResponse ->
                        followingRecyclerAdapter.differ.submitList(followingResponse)
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
        rv_following.setHasFixedSize(true)
        followingRecyclerAdapter = FollowingRecyclerAdapter()
        followingRecyclerAdapter.notifyDataSetChanged()
        rv_following.apply {
            adapter = followingRecyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        val username = arguments?.getString(ARG_USERNAME)
        if (username != null) {
            viewModel.getListFollowing(username)
        }
    }

    private fun showProgressBar() {
        progressBarDetailFollowing.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBarDetailFollowing.visibility = View.INVISIBLE
    }
}