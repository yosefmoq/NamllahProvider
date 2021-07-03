package com.app.namllahprovider.presentation.fragments.main.profile.edit_profile.dialogs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentUserEditPasswordBinding
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserEditPasswordFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var fragmentUserEditPasswordBinding: FragmentUserEditPasswordBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserEditPasswordBinding =
            FragmentUserEditPasswordBinding.inflate(inflater, container, false)
        return fragmentUserEditPasswordBinding?.apply {
            actionOnClick = this@UserEditPasswordFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeLiveData()
    }

    private fun initViews() {
        val onTextChanged = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                fragmentUserEditPasswordBinding?.etCurrentPassword?.error = null
                fragmentUserEditPasswordBinding?.etNewPassword?.error = null
                fragmentUserEditPasswordBinding?.etConfirmNewPassword?.error = null
            }
        }
        fragmentUserEditPasswordBinding?.etCurrentPassword?.addTextChangedListener(onTextChanged)
        fragmentUserEditPasswordBinding?.etNewPassword?.addTextChangedListener(onTextChanged)
        fragmentUserEditPasswordBinding?.etConfirmNewPassword?.addTextChangedListener(onTextChanged)
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
                profileViewModel.loadingLiveData.postValue(null)
            }
        })

        profileViewModel.errorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(
                    activity = requireActivity(),
                    message = it.message ?: "",
                    isJson = true,
                )
                it.printStackTrace()
            }
        })

        profileViewModel.updateProfileLiveData.observe(viewLifecycleOwner, {
            it?.let { updateUserProfileResponse ->
                Timber.tag(TAG)
                    .d("observeLiveData : updateUserProfileResponse $updateUserProfileResponse")
                if (updateUserProfileResponse.status) {
                    Toast.makeText(
                        requireContext(),
                        "Changed password successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    dismiss()
                } else {
                    val errorMessage = updateUserProfileResponse.msg
                        ?: updateUserProfileResponse.error
                        ?: "Something error, Please try again later"
                    profileViewModel.changeErrorMessage(Throwable(errorMessage))
                }
                profileViewModel.updateProfileLiveData.postValue(null)
            }
        })
    }


    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentUserEditPasswordBinding?.ibClose -> onClickClose()
            fragmentUserEditPasswordBinding?.btnSave -> onClickSave()
        }
    }

    private fun onClickClose() {
        dismiss()
    }

    private fun onClickSave() {
        val errorMessage: String
        val oldPassword = fragmentUserEditPasswordBinding?.etCurrentPassword?.text?.toString() ?: ""
        val newPassword = fragmentUserEditPasswordBinding?.etNewPassword?.text?.toString() ?: ""
        val confirmNewPassword =
            fragmentUserEditPasswordBinding?.etConfirmNewPassword?.text?.toString() ?: ""

        if (newPassword != confirmNewPassword) {
            errorMessage = "Passwords don't match"
            fragmentUserEditPasswordBinding?.etNewPassword?.error = errorMessage
            fragmentUserEditPasswordBinding?.etConfirmNewPassword?.error = errorMessage
            return
        } else if (
            oldPassword.isEmpty() || oldPassword.isBlank() || oldPassword.length < 6
        ) {
            errorMessage = "Please fill old password"
            fragmentUserEditPasswordBinding?.etCurrentPassword?.error = errorMessage
            return
        } else if (
            newPassword.isEmpty() || newPassword.isBlank() || newPassword.length < 6
        ) {
            errorMessage = "Please fill new password"
            fragmentUserEditPasswordBinding?.etNewPassword?.error = errorMessage
            return
        } else if (
            confirmNewPassword.isEmpty() || confirmNewPassword.isBlank() || confirmNewPassword.length < 6
        ) {
            errorMessage = "Please confirm new password"
            fragmentUserEditPasswordBinding?.etConfirmNewPassword?.error = errorMessage
            return
        }
        profileViewModel.updatePassword(oldPassword = oldPassword, newPassword = newPassword)
    }


    companion object {
        private const val TAG = "UserEditPasswordFragmen"
    }
}