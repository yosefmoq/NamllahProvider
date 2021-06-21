package com.app.namllahprovider.presentation.fragments.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.app.namllahprovider.databinding.FragmentEditTextBottomSheetBinding
import com.app.namllahprovider.presentation.base.BottomSheetInputType
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EditTextBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var fragmentUserEditBottomSheetBinding: FragmentEditTextBottomSheetBinding? = null

    private var title: String = ""
    private var message: String = ""
    private var hint: String = ""
    private var currentValue: String = ""
    private var bottomSheetInputType: BottomSheetInputType = BottomSheetInputType.NAME

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserEditBottomSheetBinding =
            FragmentEditTextBottomSheetBinding.inflate(inflater, container, false)
        return fragmentUserEditBottomSheetBinding?.apply {
            actionOnClick = this@EditTextBottomSheetFragment
            title = this@EditTextBottomSheetFragment.title
            message = this@EditTextBottomSheetFragment.message
            hint = this@EditTextBottomSheetFragment.hint
            currentValue = this@EditTextBottomSheetFragment.currentValue
            inputType = this@EditTextBottomSheetFragment.bottomSheetInputType.idKey
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData() {
        profileViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("observeLiveData : Loading Status $it")
                if (it) {
                    SweetAlert.instance.showAlertDialog(
                        context = requireContext(),
                        alertType = SweetAlertType.PROGRESS_TYPE,
                        title = "Loading",
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

        profileViewModel.updateProfileLiveData.observe(viewLifecycleOwner) {
            it?.let { updateUserProfileResponse ->
                Timber.tag(TAG)
                    .d("observeLiveData : updateUserProfileResponse $updateUserProfileResponse")
                if (updateUserProfileResponse.status) {
                    dismiss()
                } else {
                    val errorMessage =
                        updateUserProfileResponse.errors?.mobile?.first()
                            ?: updateUserProfileResponse.msg
                            ?: "Something error, Please try again later"
                    profileViewModel.changeErrorMessage(Throwable(errorMessage))
                }
                profileViewModel.updateProfileLiveData.postValue(null)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = EditTextBottomSheetFragmentArgs.fromBundle(it).title
            message = EditTextBottomSheetFragmentArgs.fromBundle(it).message
            hint = EditTextBottomSheetFragmentArgs.fromBundle(it).hint
            currentValue = EditTextBottomSheetFragmentArgs.fromBundle(it).currentValue
            bottomSheetInputType = EditTextBottomSheetFragmentArgs.fromBundle(it).inputType
        }
    }


    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentUserEditBottomSheetBinding?.ibClose -> onClickClose()
            fragmentUserEditBottomSheetBinding?.btnSave -> onClickSave()
        }
    }

    private fun onClickClose() {
        dismiss()
    }

    private fun onClickSave() {
        val newValue = fragmentUserEditBottomSheetBinding?.etUserEditField?.text?.toString()
        newValue?.let {
            when (bottomSheetInputType) {
                BottomSheetInputType.NAME -> onClickSaveName(newValue)
                BottomSheetInputType.PHONE -> onClickSavePhone(newValue)
                BottomSheetInputType.ADDRESS -> onClickSaveAddress(newValue)
            }
        }

    }

    private fun onClickSaveName(newValue: String) {
        Timber.tag(TAG).d("onClickSaveName : ttt $newValue")
        profileViewModel.updateUsername(newName = newValue)
    }

    private fun onClickSavePhone(newValue: String) {
        profileViewModel.updatePhoneNumber(newPhone = newValue)
    }

    private fun onClickSaveAddress(newValue: String) {

    }

    companion object {
        private const val TAG = "EditTextBottomSheetFrag"
    }
}