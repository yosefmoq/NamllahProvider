package com.app.namllahprovider.presentation.fragments.common.radio_list_bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.databinding.ItemRadioListBottomSheetBinding

class RadioListBottomSheetAdapter(
    var radioList: MutableList<Pair<Boolean, String>>,
) : RecyclerView.Adapter<RadioListBottomSheetAdapter.RadioListBottomSheetViewHolder>() {


    class RadioListBottomSheetViewHolder(val view: ItemRadioListBottomSheetBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bindView(text: String) {
            view.text = text
        }
    }

    fun updateData(list: MutableList<Pair<Boolean, String>>) {
        this.radioList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RadioListBottomSheetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRadioListBottomSheetBinding.inflate(layoutInflater, parent, false)
        return RadioListBottomSheetViewHolder(binding)
    }

    override fun onBindViewHolder(holderRadio: RadioListBottomSheetViewHolder, position: Int) {
        val currentItem = radioList[position]
        val currentItemText = currentItem.second
        holderRadio.bindView(
            text = currentItemText,
        )
        holderRadio.view.cbListItemText.setOnCheckedChangeListener(null)
        holderRadio.view.cbListItemText.isChecked = currentItem.first
        holderRadio.view.cbListItemText.setOnCheckedChangeListener { _, isChecked ->
            radioList[position] = Pair(isChecked, currentItemText)
        }
    }

    override fun getItemCount(): Int = radioList.size
}