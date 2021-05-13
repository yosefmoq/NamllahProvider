package com.app.namllahprovider.presentation.fragments.main.profile.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentUserServiceBinding
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserServiceFragment : Fragment() {

    private var fragmentUserServiceBinding: FragmentUserServiceBinding? = null

    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserServiceBinding = FragmentUserServiceBinding.inflate(inflater, container, false)
        return fragmentUserServiceBinding?.apply {  }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }
    private fun initToolbar() {
        val toolbar = fragmentUserServiceBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.my_services)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val TAG = "UserServiceFragment"

        @JvmStatic
        fun newInstance() = UserServiceFragment()
    }
}