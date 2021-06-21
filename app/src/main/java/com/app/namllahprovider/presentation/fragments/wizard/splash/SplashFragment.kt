package com.app.namllahprovider.presentation.fragments.wizard.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentSplashBinding
import com.app.namllahprovider.presentation.MainActivity
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
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(1500)
            moveToNextUI()
        }
    }

    private fun moveToNextUI() {
        Timber.tag(TAG).d("moveToNextUI : ")
        val isLogin = splashViewModel.isLogin()
        Timber.tag(TAG).d("moveToNextUI : ")
        val isSeenOnBoarding = splashViewModel.isSeenOnBoarding()
        if (isLogin) {
            //Go to Main
            startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()
        } else {
            val destination = if (!isSeenOnBoarding) {
                //Go to Boarding Screen
                R.id.action_splashFragment_to_onBoardingFragment
            } else {
                //Go to Login Screen
                R.id.action_splashFragment_to_signInFragment
            }
            findNavController().navigate(destination)
        }

    }

    companion object {
        private const val TAG = "SplashFragment"
    }
}