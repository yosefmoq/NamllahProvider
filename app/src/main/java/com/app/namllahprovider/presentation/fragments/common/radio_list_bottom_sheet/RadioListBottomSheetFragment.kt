package com.app.namllahprovider.presentation.fragments.common.radio_list_bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.databinding.FragmentRadioListBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RadioListBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var fragmentRadioListBottomSheetBinding: FragmentRadioListBottomSheetBinding? = null

    private var title: String = ""
    private var message: String = ""
    private var hint: String = ""
    private var radioList = mutableListOf<Pair<Boolean, String>>()
    private var textList: List<String> = listOf()
    private var selectedPositionList: List<Int> = listOf()

    private var radioListSelectionItem: RadioListSelectionItem? = null

    private var radioListBottomSheetAdapter = RadioListBottomSheetAdapter(radioList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentRadioListBottomSheetBinding =
            FragmentRadioListBottomSheetBinding.inflate(inflater, container, false)
        return fragmentRadioListBottomSheetBinding?.apply {
            actionOnClick = this@RadioListBottomSheetFragment
            title = this@RadioListBottomSheetFragment.title
            message = this@RadioListBottomSheetFragment.message
            hintSearch = this@RadioListBottomSheetFragment.hint
            radioBottomSheetAdapter = this@RadioListBottomSheetFragment.radioListBottomSheetAdapter
            layoutManger = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }?.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = RadioListBottomSheetFragmentArgs.fromBundle(it).title
            message = RadioListBottomSheetFragmentArgs.fromBundle(it).message
            hint = RadioListBottomSheetFragmentArgs.fromBundle(it).hint
            textList = RadioListBottomSheetFragmentArgs.fromBundle(it).textArray.toMutableList()
            selectedPositionList =
                RadioListBottomSheetFragmentArgs.fromBundle(it).selectedPositionArray.toMutableList()
            radioListSelectionItem =
                RadioListBottomSheetFragmentArgs.fromBundle(it).radioListSelectionItem
        }
        radioList.clear()
        for (position in textList.indices) {
            radioList.add(Pair(selectedPositionList.contains(position), textList[position]))
        }
        radioListBottomSheetAdapter.updateData(radioList)
    }


    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentRadioListBottomSheetBinding?.ibClose -> onClickClose()
            fragmentRadioListBottomSheetBinding?.btnSave -> onClickSave()
        }
    }

    private fun onClickClose() {
        dismiss()
    }

    private fun onClickSave() {
        val intArray = mutableListOf<Int>()
        for (position in radioListBottomSheetAdapter.radioList.indices) {
            if (radioListBottomSheetAdapter.radioList[position].first)
                intArray.add(position)
        }
        radioListSelectionItem?.let { it.selectionItems(intArray.toIntArray()) }
        dismiss()
    }

    companion object {
        private const val TAG = "ListBottomSheetFragment"
    }

}