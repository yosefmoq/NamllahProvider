package com.app.namllahprovider.presentation.fragments.main.profile.edit_profile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.namllahprovider.databinding.FragmentUserEditBottomSheetBinding
import com.app.namllahprovider.presentation.base.BottomSheetInputType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserEditBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var fragmentUserEditBottomSheetBinding: FragmentUserEditBottomSheetBinding? = null

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
            FragmentUserEditBottomSheetBinding.inflate(inflater, container, false)
        return fragmentUserEditBottomSheetBinding?.apply {
            actionOnClick = this@UserEditBottomSheetFragment
            title = this@UserEditBottomSheetFragment.title
            message = this@UserEditBottomSheetFragment.message
            hint = this@UserEditBottomSheetFragment.hint
            currentValue = this@UserEditBottomSheetFragment.currentValue
            inputType = this@UserEditBottomSheetFragment.bottomSheetInputType.idKey
        }?.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = UserEditBottomSheetFragmentArgs.fromBundle(it).title
            message = UserEditBottomSheetFragmentArgs.fromBundle(it).message
            hint = UserEditBottomSheetFragmentArgs.fromBundle(it).hint
            currentValue = UserEditBottomSheetFragmentArgs.fromBundle(it).currentValue
            bottomSheetInputType = UserEditBottomSheetFragmentArgs.fromBundle(it).inputType
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
        val newValue = fragmentUserEditBottomSheetBinding?.etBottomSheet?.text?.toString()
        newValue?.let {
            when (bottomSheetInputType) {
                BottomSheetInputType.NAME -> onClickSaveName(newValue)
                BottomSheetInputType.PHONE -> onClickSavePhone(newValue)
                BottomSheetInputType.ADDRESS -> onClickSaveAddress(newValue)
            }
        }

    }

    private fun onClickSaveName(newValue: String) {

    }

    private fun onClickSavePhone(newValue: String) {

    }

    private fun onClickSaveAddress(newValue: String) {

    }
}