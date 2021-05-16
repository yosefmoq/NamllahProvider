package com.app.namllahprovider.presentation.fragments.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.namllahprovider.databinding.FragmentEditTextBottomSheetBinding
import com.app.namllahprovider.presentation.base.BottomSheetInputType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditTextBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

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

    }

    private fun onClickSavePhone(newValue: String) {

    }

    private fun onClickSaveAddress(newValue: String) {

    }
}