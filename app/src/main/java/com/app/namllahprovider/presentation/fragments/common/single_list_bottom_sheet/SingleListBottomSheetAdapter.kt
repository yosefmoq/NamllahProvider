package com.app.namllahprovider.presentation.fragments.common.single_list_bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.databinding.ItemSingleListBottomSheetBinding

class SingleListBottomSheetAdapter(
    private var list: List<String>,
    private val onSingleListBottomSheetListener: OnSingleListBottomSheetListener
) : RecyclerView.Adapter<SingleListBottomSheetAdapter.ListBottomSheetViewHolder>() {

    fun updateData(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }
    class ListBottomSheetViewHolder(val view: ItemSingleListBottomSheetBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bindView(
            position: Int,
            text: String,
            onSingleListBottomSheetListener: OnSingleListBottomSheetListener
        ) {
            view.position = position
            view.text = text
            view.onListBottomSheetListener = onSingleListBottomSheetListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBottomSheetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSingleListBottomSheetBinding.inflate(layoutInflater, parent, false)
        return ListBottomSheetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListBottomSheetViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bindView(
            position = position,
            text = currentItem,
            onSingleListBottomSheetListener = onSingleListBottomSheetListener
        )
    }

    override fun getItemCount(): Int = list.size
}