package com.app.namllahprovider.presentation.fragments.wizard.on_boarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentOnBoardingBinding
import java.util.*


class OnBoardingFragment : Fragment(), View.OnClickListener {

    private var fragmentOnBoardingBinding: FragmentOnBoardingBinding? = null
    private val lastIndex = 2
    private var currentIndex = 0
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
        initViews()
    }

    private fun initViews() {
        fragmentOnBoardingBinding?.pageIndicatorView?.apply {
            selection = currentIndex
            isSoundEffectsEnabled = true
        }
        fragmentOnBoardingBinding?.viewPager?.let {
            val adapter = OnBoardingAdapter()
            adapter.data = createPageList()
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

    private fun createPageList(): List<View> {
        val pageList: MutableList<View> = ArrayList()
        val screen1 = LayoutInflater.from(context).inflate(R.layout.view_on_boarding_1, null)
        val screen2 = LayoutInflater.from(context).inflate(R.layout.view_on_boarding_2, null)
        val screen3 = LayoutInflater.from(context).inflate(R.layout.view_on_boarding_3, null)
        pageList.add(screen1)
        pageList.add(screen2)
        pageList.add(screen3)
        return pageList
    }

    companion object {
        private const val TAG = "OnBoardingFragment"
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentOnBoardingBinding?.tvNext -> {
                fragmentOnBoardingBinding?.viewPager?.currentItem = currentIndex + 1
            }
            fragmentOnBoardingBinding?.tvSkip ,  fragmentOnBoardingBinding?.btnGetStarted-> {
                findNavController().navigate(R.id.action_onBoardingFragment_to_signUpFragment)
            }
        }
    }
}