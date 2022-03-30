package com.app.namllahprovider.presentation.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentMainBinding
import com.app.namllahprovider.presentation.fragments.main.home.HomeFragment
import com.app.namllahprovider.presentation.fragments.main.notifiactions.NotificationFragment
import com.app.namllahprovider.presentation.fragments.main.profile.profile.UserProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var fragmentMainBinding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return fragmentMainBinding?.apply { }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMainBinding?.navigation?.setOnNavigationItemSelectedListener(this)
        loadFragment(currentFragmentType)
    }

    private fun loadFragment(fragmentType: FragmentType) {
        currentFragmentType = fragmentType
        val destinationFragment = when (currentFragmentType) {
            FragmentType.HOME -> HomeFragment.newInstance()
            FragmentType.NOTIFICATION -> NotificationFragment.newInstance()
            FragmentType.PROFILE -> UserProfileFragment.newInstance()
        }
        Timber.tag(TAG).d("loadFragment : fragment type : $currentFragmentType")
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(fragmentMainBinding?.frameContainer?.id!!, destinationFragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_home -> loadFragment(FragmentType.HOME)
            R.id.mi_notification -> loadFragment(FragmentType.NOTIFICATION)
            R.id.mi_profile -> loadFragment(FragmentType.PROFILE)
        }
        return true
    }

    enum class FragmentType {
        HOME, NOTIFICATION, PROFILE
    }

    companion object {
        private const val TAG = "MainFragment"
        var currentFragmentType = FragmentType.HOME
    }
}