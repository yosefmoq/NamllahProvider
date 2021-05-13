package com.app.namllahprovider.presentation.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

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
    }

    private fun loadFragmentByNavigation(bottomNavType: BottomNavType) {
        val destination = when (bottomNavType) {
            BottomNavType.HOME -> R.id.action_global_homeFragment
            BottomNavType.NOTIFICATION -> R.id.action_global_notificationFragment
            BottomNavType.PROFILE -> R.id.action_global_profileFragment
        }
        val navHostFragment = childFragmentManager.findFragmentById(R.id.main_nav) as NavHostFragment
        navHostFragment.navController.navigate(destination)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_home -> loadFragmentByNavigation(BottomNavType.HOME)
            R.id.mi_notification -> loadFragmentByNavigation(BottomNavType.NOTIFICATION)
            R.id.mi_profile -> loadFragmentByNavigation(BottomNavType.PROFILE)
        }
        return true
    }

    enum class BottomNavType {
        HOME, NOTIFICATION, PROFILE
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}