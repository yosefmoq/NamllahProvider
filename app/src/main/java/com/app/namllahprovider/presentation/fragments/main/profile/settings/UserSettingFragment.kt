package com.app.namllahprovider.presentation.fragments.main.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.databinding.FragmentUserSettingBinding
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint

class UserSettingFragment : Fragment(), View.OnClickListener {

    private var fragmentUserSettingBinding: FragmentUserSettingBinding? = null

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var userDto: UserDto? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserSettingBinding = FragmentUserSettingBinding.inflate(inflater, container, false)
        return fragmentUserSettingBinding?.apply {
            actionOnClick = this@UserSettingFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        getLoggedProfile()
        initCurrentSettings()
    }

    private fun initCurrentSettings() {

    }

    private fun getLoggedProfile() {
        profileViewModel.getLoggedUserApi()
        profileViewModel.getLoggedUserLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("getLoggedProfile : it $it")
                userDto = it
                fragmentUserSettingBinding?.notificationStatus =
                    userDto?.settings?.notification ?: "false" == "true"
                fragmentUserSettingBinding?.switchUserNotification?.setOnCheckedChangeListener { _, isChecked ->
                    profileViewModel.updateSettings("notification", isChecked)
                }
                profileViewModel.getLoggedUserLiveData.postValue(null)
            }
        })
    }

    private fun initToolbar() {
        val toolbar = fragmentUserSettingBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.settings)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val TAG = "UserSettingFragment"

        @JvmStatic
        fun newInstance() = UserSettingFragment()
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentUserSettingBinding?.tvEditLanguage -> onClickEditLanguage()
        }
    }

    private fun onClickEditLanguage() {
        Timber.tag(TAG).d("onClickEditLanguage : language $userDto")
        findNavController().navigate(
            UserSettingFragmentDirections.actionUserSettingFragmentToLanguageListBottomSheetFragment(
                currentLanguage = (userDto?.language?.id ?: 1).toInt()
            )
        )
    }
}