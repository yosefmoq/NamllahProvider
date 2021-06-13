package com.app.namllahprovider.presentation.fragments.main.profile.edit_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.databinding.FragmentUserEditProfileBinding
import com.app.namllahprovider.presentation.base.BottomSheetInputType
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserEditProfileFragment : Fragment(), View.OnClickListener {

    private var fragmentUserEditProfileBinding: FragmentUserEditProfileBinding? = null
    private val profileViewModel: ProfileViewModel by viewModels()

    private var userDto: UserDto? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserEditProfileBinding =
            FragmentUserEditProfileBinding.inflate(inflater, container, false)
        return fragmentUserEditProfileBinding?.apply {
            actionOnCLick = this@UserEditProfileFragment
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
                userDto = it
                fragmentUserEditProfileBinding?.userDto = userDto
                profileViewModel.getLoggedUserLiveData.postValue(null)
            }
        })
    }

    private fun initToolbar() {
        val toolbar = fragmentUserEditProfileBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.edit_profile)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val TAG = "UserEditProfileFragment"
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentUserEditProfileBinding?.clUsername -> onClickEditUsername()
            fragmentUserEditProfileBinding?.clPhoneNumber -> onClickEditPhoneNumber()
            fragmentUserEditProfileBinding?.clPassword -> onClickEditPassword()
            fragmentUserEditProfileBinding?.clAddress -> onClickEditAddress()
            fragmentUserEditProfileBinding?.clNationality -> onClickEditNationality()
        }
    }

    private fun onClickEditUsername() {
        findNavController().navigate(
            UserEditProfileFragmentDirections.actionUserEditProfileFragmentToUserEditBottomSheetFragment(
                title = getString(R.string.change_username_title),
                message = getString(R.string.change_username_message),
                hint = getString(R.string.username),
                currentValue = userDto?.name ?: "",
                inputType = BottomSheetInputType.NAME
            )
        )
    }

    private fun onClickEditPhoneNumber() {
        findNavController().navigate(
            UserEditProfileFragmentDirections.actionUserEditProfileFragmentToUserEditBottomSheetFragment(
                title = getString(R.string.change_phone_number_title),
                message = getString(R.string.change_phone_number_message),
                hint = getString(R.string.phone_number),
                currentValue = userDto?.mobile ?: "",
                inputType = BottomSheetInputType.PHONE
            )
        )
    }

    private fun onClickEditPassword() {
        findNavController().navigate(
            UserEditProfileFragmentDirections.actionUserEditProfileFragmentToUserEditPasswordFragment()
        )
    }

    private fun onClickEditAddress() {
        findNavController().navigate(
            UserEditProfileFragmentDirections.actionUserEditProfileFragmentToUserEditBottomSheetFragment(
                title = getString(R.string.change_address_title),
                message = getString(R.string.change_address_message),
                hint = getString(R.string.address),
                currentValue = userDto?.type ?: "",
                inputType = BottomSheetInputType.ADDRESS
            )
        )
    }

    private fun onClickEditNationality() {
        Timber.tag(TAG).d("onClickEditNationality    : ")
    }
}