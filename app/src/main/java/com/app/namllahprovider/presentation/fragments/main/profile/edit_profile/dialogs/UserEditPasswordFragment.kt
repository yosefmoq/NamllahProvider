package com.app.namllahprovider.presentation.fragments.main.profile.edit_profile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.namllahprovider.databinding.FragmentUserEditPasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserEditPasswordFragment : BottomSheetDialogFragment(), View.OnClickListener {

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


    }

}