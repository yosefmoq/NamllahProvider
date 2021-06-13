package com.app.namllahprovider.presentation.fragments.main.profile.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentUserProfileBinding
import com.app.namllahprovider.presentation.fragments.main.MainFragmentDirections
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserProfileFragment : Fragment(), View.OnClickListener {

    private var fragmentUserProfileBinding: FragmentUserProfileBinding? = null
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserProfileBinding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return fragmentUserProfileBinding?.apply {
            actionOnClick = this@UserProfileFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        getLoggedProfile()
    }

    private fun getLoggedProfile() {
        profileViewModel.getLoggedUser()
        profileViewModel.getLoggedUserLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("getLoggedProfile : it $it")
                fragmentUserProfileBinding?.userDto = it
                profileViewModel.getLoggedUserLiveData.postValue(null)
            }
        })
    }


    private fun initToolbar() {
        val toolbar = fragmentUserProfileBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.profile)

        toolbar?.root?.navigationIcon = null
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentUserProfileBinding?.tvEditProfile -> onClickEditProfile()
            fragmentUserProfileBinding?.tvUserServices -> onClickUserServices()
            fragmentUserProfileBinding?.tvUserSettings -> onClickUserSettings()
            fragmentUserProfileBinding?.tvUserLogout -> onClickLogout()
        }
    }

    private fun onClickEditProfile() {
        findNavController().navigate(MainFragmentDirections.actionProfileFragmentToUserEditProfileFragment())
    }

    private fun onClickUserServices() {
        findNavController().navigate(MainFragmentDirections.actionProfileFragmentToUserServiceFragment())
    }

    private fun onClickUserSettings() {
        findNavController().navigate(MainFragmentDirections.actionProfileFragmentToUserSettingFragment())
    }

    private fun onClickLogout() {
        //Send Request To API

        //

        //Delete Logged User From SP
    }

    companion object {
        private const val TAG = "ProfileFragment"

        @JvmStatic
        fun newInstance() = UserProfileFragment()
    }
}