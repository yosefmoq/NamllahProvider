package com.app.namllahprovider.presentation.fragments.wizard.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var fragmentSplashBinding: FragmentSplashBinding? = null

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSplashBinding = FragmentSplashBinding.inflate(inflater, container, false)
        return fragmentSplashBinding?.apply { }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag(TAG).d("onViewCreated : printInLaunch")
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(2000)
            moveToNextUI()
        }

        GlobalScope.launch(context = Dispatchers.Main) {
            //Do Other Action like check network and initial requests from server
        }

    }

    private fun moveToNextUI() {
        Timber.tag(TAG).d("moveToNextUI : ")
        val isLogin = splashViewModel.isLogin()
        val isSeenOnBoarding = splashViewModel.isSeenOnBoarding()
        val destination = if (isLogin) {
            //Go to Main
            R.id.action_splashFragment_to_mainFragment
        } else {
            if (!isSeenOnBoarding) {
                //Go to Boarding Screen
                R.id.action_splashFragment_to_onBoardingFragment
            } else {
                //Go to Login Screen
                R.id.action_splashFragment_to_signInFragment
            }
        }
        findNavController().navigate(destination)
    }

    companion object {
        private const val TAG = "SplashFragment"
    }
}