package com.app.namllahprovider.presentation.fragments.main.home.check_timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.namllahprovider.databinding.FragmentWorkItemBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class WorkItemBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var fragmentWorkItemBottomSheetBinding: FragmentWorkItemBottomSheetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentWorkItemBottomSheetBinding =
            FragmentWorkItemBottomSheetBinding.inflate(inflater, container, false)
        return fragmentWorkItemBottomSheetBinding?.apply {
            actionOnClick = this@WorkItemBottomSheetFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private const val TAG = "WorkItemFragment"

        @JvmStatic
        fun newInstance() = WorkItemBottomSheetFragment()

    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentWorkItemBottomSheetBinding?.ibClose -> onClickClose()
        }
    }
    private fun onClickClose() {
        dismiss()
    }
}