package com.app.namllahprovider.presentation.fragments.wizard.on_boarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class OnBoardingFragment : Fragment(), View.OnClickListener {

    private var fragmentOnBoardingBinding: FragmentOnBoardingBinding? = null
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    private val lastIndex = 2
    private var currentIndex = 0
    var screen1: View? = null
    var screen2: View? = null
    var screen3: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentOnBoardingBinding = FragmentOnBoardingBinding.inflate(inflater, container, false)


        return fragmentOnBoardingBinding?.apply {
            actionOnClick = this@OnBoardingFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreens()
        initViews()
    }

    private fun initScreens() {
        screen1 = LayoutInflater.from(context).inflate(R.layout.view_on_boarding_1, null)
        screen2 = LayoutInflater.from(context).inflate(R.layout.view_on_boarding_2, null)
        screen3 = LayoutInflater.from(context).inflate(R.layout.view_on_boarding_3, null)
    }

    private fun initViews() {
        fragmentOnBoardingBinding?.pageIndicatorView?.apply {
            selection = currentIndex
            isSoundEffectsEnabled = true
        }
        fragmentOnBoardingBinding?.viewPager?.let {
            val adapter = OnBoardingAdapter()
            adapter.data = listOf(screen1!!, screen2!!, screen3!!)
            it.adapter = adapter
            it.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    currentIndex = position
                    fragmentOnBoardingBinding?.pageIndicatorView?.selection = position
                    if (position != lastIndex) {
                        fragmentOnBoardingBinding?.tvSkip?.visibility = View.VISIBLE
                        fragmentOnBoardingBinding?.tvNext?.visibility = View.VISIBLE
                        fragmentOnBoardingBinding?.btnGetStarted?.visibility = View.GONE

                    } else {
                        fragmentOnBoardingBinding?.tvSkip?.visibility = View.GONE
                        fragmentOnBoardingBinding?.tvNext?.visibility = View.GONE
                        fragmentOnBoardingBinding?.btnGetStarted?.visibility = View.VISIBLE
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }
    }

    companion object {
        private const val TAG = "OnBoardingFragment"
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentOnBoardingBinding?.tvNext -> {
                fragmentOnBoardingBinding?.viewPager?.currentItem = currentIndex + 1
            }
            fragmentOnBoardingBinding?.tvSkip, fragmentOnBoardingBinding?.btnGetStarted -> {
                onBoardingViewModel.changeOnBoardingStatus(true)
                findNavController().navigate(R.id.action_onBoardingFragment_to_signInFragment)
            }
        }
    }
}