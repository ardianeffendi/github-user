package com.ardianeffendi.consumer_github.adapters

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ardianeffendi.consumer_github.R
import com.ardianeffendi.consumer_github.ui.fragments.FollowersFragment
import com.ardianeffendi.consumer_github.ui.fragments.FollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username: String? = null

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.followers_tab,
        R.string.following_tab
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment =
                username?.let { FollowersFragment.newInstance(it) }
            1 -> fragment =
                username?.let { FollowingFragment.newInstance(it) }
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}