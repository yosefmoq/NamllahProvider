package com.app.namllahprovider.presentation.fragments.main.home.cancel_reasons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.CancelReasonDto
import com.app.namllahprovider.databinding.ItemCancelReasonsBinding
import timber.log.Timber

class CancelReasonAdapter(
    val onCheckedChangeListener: OnCheckedChangeListener
) : RecyclerView.Adapter<CancelReasonAdapter.CancelReasonViewHolder>() {

    private var cancelReasonList = listOf<CancelReasonDto>()
    private var currentSelectPosition = -1

    fun getList() = cancelReasonList
    fun getCurrentSelectPosition() = currentSelectPosition

    fun updateData(list: List<CancelReasonDto>) {
        cancelReasonList = list
        notifyDataSetChanged()
    }

    fun updateCurrentSelected(position: Int) {
        Timber.tag(TAG).d("updateCurrentSelected : currentSelectPosition $currentSelectPosition")
        notifyItemChanged(currentSelectPosition)
        currentSelectPosition = position
        notifyItemChanged(currentSelectPosition)
        Timber.tag(TAG).d("updateCurrentSelected : currentSelectPosition $currentSelectPosition")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancelReasonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCancelReasonsBinding.inflate(inflater, parent, false)
        return CancelReasonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CancelReasonViewHolder, position: Int) {
        val currentCancelReason = cancelReasonList[position]
        holder.bindView(currentCancelReason, position)
    }

    override fun getItemCount(): Int = cancelReasonList.size

    inner class CancelReasonViewHolder(val view: ItemCancelReasonsBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bindView(currentCancelReason: CancelReasonDto, currentPosition: Int) {
            Timber.tag(TAG).d("bindView : currentCancelReason $currentCancelReason")
            Timber.tag(TAG).d("bindView : currentCancelReason isChecked ${currentPosition == currentSelectPosition}")
            view.apply {
                cancelReasonDto = currentCancelReason
                position = currentPosition
                isChecked = currentPosition == currentSelectPosition
                onCheckedChangeListener = this@CancelReasonAdapter.onCheckedChangeListener
            }
        }
    }

    companion object {
        private const val TAG = "CancelReasonAdapter"
    }

}