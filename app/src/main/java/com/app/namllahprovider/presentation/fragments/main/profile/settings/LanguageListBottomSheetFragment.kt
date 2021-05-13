package com.app.namllahprovider.presentation.fragments.main.profile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.namllahprovider.databinding.FragmentLanguageListBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguageListBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var fragmentUserEditBottomSheetBinding: FragmentLanguageListBottomSheetBinding? = null

    private var currentLanguage: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserEditBottomSheetBinding =
            FragmentLanguageListBottomSheetBinding.inflate(inflater, container, false)
        return fragmentUserEditBottomSheetBinding?.apply {
            actionOnClick = this@LanguageListBottomSheetFragment
        }?.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentLanguage = LanguageListBottomSheetFragmentArgs.fromBundle(it).currentLanguage
        }
        fragmentUserEditBottomSheetBinding?.currentLanguage = currentLanguage
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

    }

}