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
import com.app.namllahprovider.databinding.FragmentUserEditProfileBinding
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserEditProfileFragment : Fragment(), View.OnClickListener {

    private var fragmentUserEditProfileBinding: FragmentUserEditProfileBinding? = null
    private val profileViewModel: ProfileViewModel by viewModels()

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
        Timber.tag(TAG).d("onClickEditUsername   : ")
    }

    private fun onClickEditPhoneNumber() {
        Timber.tag(TAG).d("onClickEditPhoneNumber    : ")
    }

    private fun onClickEditPassword() {
        Timber.tag(TAG).d("onClickEditPassword   : ")
    }

    private fun onClickEditAddress() {
        Timber.tag(TAG).d("onClickEditAddress    : ")
    }

    private fun onClickEditNationality() {
        Timber.tag(TAG).d("onClickEditNationality    : ")
    }
}