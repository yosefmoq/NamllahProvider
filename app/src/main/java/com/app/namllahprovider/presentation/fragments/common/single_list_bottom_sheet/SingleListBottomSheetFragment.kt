package com.app.namllahprovider.presentation.fragments.common.single_list_bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.databinding.FragmentSingleListBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

class SingleListBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener,
    OnSingleListBottomSheetListener {

    private var fragmentSingleListBottomSheetBinding: FragmentSingleListBottomSheetBinding? = null

    private var title: String = ""
    private var message: String = ""
    private var hint: String = ""
    private var currentValue: String = ""
    private var textList: List<String> = listOf()
    private var singleListSelectionItem: SingleListSelectionItem? = null

    private var listBottomSheetAdapter = SingleListBottomSheetAdapter(textList, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSingleListBottomSheetBinding =
            FragmentSingleListBottomSheetBinding.inflate(inflater, container, false)
        return fragmentSingleListBottomSheetBinding?.apply {
            actionOnClick = this@SingleListBottomSheetFragment
            title = this@SingleListBottomSheetFragment.title
            message = this@SingleListBottomSheetFragment.message
            hintSearch = this@SingleListBottomSheetFragment.hint
            currentValue = this@SingleListBottomSheetFragment.currentValue
            listBottomSheetAdapter = this@SingleListBottomSheetFragment.listBottomSheetAdapter
            layoutManger = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }?.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = SingleListBottomSheetFragmentArgs.fromBundle(it).title
            message = SingleListBottomSheetFragmentArgs.fromBundle(it).message
            hint = SingleListBottomSheetFragmentArgs.fromBundle(it).hint
            currentValue = SingleListBottomSheetFragmentArgs.fromBundle(it).currentValue
            textList = SingleListBottomSheetFragmentArgs.fromBundle(it).textArray.toList()
            singleListSelectionItem = SingleListBottomSheetFragmentArgs.fromBundle(it).singleListSelectionItem
        }
        listBottomSheetAdapter.updateData(textList)
    }


    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentSingleListBottomSheetBinding?.ibClose -> onClickClose()
            fragmentSingleListBottomSheetBinding?.btnSave -> onClickSave()
        }
    }

    private fun onClickClose() {
        dismiss()
    }

    private fun onClickSave() {

    }

    override fun onChooseItem(position: Int) {
        Timber.tag(TAG).d("onChooseItem : position $position ${textList[position]}")
        singleListSelectionItem?.let { it.selectionItem(position) }
        dismiss()
    }

    companion object {
        private const val TAG = "ListBottomSheetFragment"
    }
}