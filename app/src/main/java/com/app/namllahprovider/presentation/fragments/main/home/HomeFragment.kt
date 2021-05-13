package com.app.namllahprovider.presentation.fragments.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app.namllahprovider.databinding.FragmentHomeBinding
import com.app.namllahprovider.presentation.base.ViewPagerAdapter
import com.app.namllahprovider.presentation.fragments.main.home.finished_order.FinishedOrderFragment
import com.app.namllahprovider.presentation.fragments.main.home.in_progress_order.InProgressOrderFragment
import com.app.namllahprovider.presentation.fragments.main.home.new_order.NewOrderFragment
import timber.log.Timber

class HomeFragment : Fragment() {

    private var fragmentHomeBinding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.tag(TAG).d("onCreateView : ")
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding?.apply { }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.tag(TAG).d("onViewCreated : ")
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding?.tlHomeTabLayout?.setupWithViewPager(
            fragmentHomeBinding?.vpHomeViewPager ?: return
        )
        setupViewPager(fragmentHomeBinding?.vpHomeViewPager ?: return)
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NewOrderFragment.newInstance(), "New")
        adapter.addFragment(InProgressOrderFragment.newInstance(), "In Progress")
        adapter.addFragment(FinishedOrderFragment.newInstance(), "Finish")
        viewPager.adapter = adapter
    }

    companion object {
        private const val TAG = "HomeFragment"

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}