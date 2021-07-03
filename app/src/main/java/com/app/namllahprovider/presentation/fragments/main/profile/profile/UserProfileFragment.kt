package com.app.namllahprovider.presentation.fragments.main.profile.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentUserProfileBinding
import com.app.namllahprovider.presentation.WizardActivity
import com.app.namllahprovider.presentation.fragments.main.MainFragmentDirections
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import com.app.namllahprovider.presentation.utils.getAddressFromLatAndLng
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserProfileFragment : Fragment(), View.OnClickListener {

    private var fragmentUserProfileBinding: FragmentUserProfileBinding? = null
    private val profileViewModel: ProfileViewModel by activityViewModels()

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
        observeLiveData()
        getLoggedProfile()
    }

    private fun getLoggedProfile() {
        profileViewModel.getLoggedUserApi()
        profileViewModel.getLoggedUserLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("getLoggedProfile : it $it")
                val address = getAddressFromLatAndLng(
                    requireContext(),
                    it.lat?.toDoubleOrNull() ?: 0.0,
                    it.lng?.toDoubleOrNull() ?: 0.0
                )
                Timber.tag(TAG).d("getLoggedProfile : address $address")
                fragmentUserProfileBinding?.userDto = it
                fragmentUserProfileBinding?.address = address
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

    private fun observeLiveData() {
        profileViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("observeLiveData : Loading Status $it")
                if (it) {
                    SweetAlert.instance.showAlertDialog(
                        context = requireContext(),
                        alertType = SweetAlertType.PROGRESS_TYPE,
                        title = getString(R.string.loading),
                        message = "",
                        confirmText = "",
                        confirmListener = {},
                        cancelText = "",
                        cancelListener = {},
                        cancelable = false,
                    )
                } else {
                    SweetAlert.instance.dismissAlertDialog(true)
                }
            }
        })

        profileViewModel.errorLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(activity = requireActivity(), throwable = it)
                it.printStackTrace()
            }
        }
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
        //Delete Logged User From SP
        SweetAlert.instance.showAlertDialog(
            context = requireContext(),
            alertType = SweetAlertType.WARNING_TYPE,
            title = "Logout?",
            message = "Are you sure you want to logout?",
            confirmText = "Logout",
            confirmListener = {
                profileViewModel.logoutLiveData.observe(viewLifecycleOwner) {
                    it?.let {
                        if (it.status!!) {
                            //clear config data
                            profileViewModel.clearConfigData()

                            //move to login UI
                            startActivity(Intent(context, WizardActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                }
                profileViewModel.logout()
            },
            cancelText = "Stay",
            cancelListener = {

            },
            cancelable = true,
        )

    }

    companion object {
        private const val TAG = "ProfileFragment"

        @JvmStatic
        fun newInstance() = UserProfileFragment()
    }
}