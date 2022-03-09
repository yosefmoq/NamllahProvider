package com.app.namllahprovider.presentation.fragments.main.home.finished_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.ItemFinishedOrderBinding

class FinishedOrderAdapter(
    private var finishedOrderList: List<OrderDto>,
    private val onFinishedOrderListener: OnFinishedOrderListener
) : RecyclerView.Adapter<FinishedOrderAdapter.FinishedOrderViewHolder>() {

    fun updateData(finishedOrderList: List<OrderDto>) {
        this.finishedOrderList = finishedOrderList
        notifyItemRangeInserted(0,finishedOrderList.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedOrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFinishedOrderBinding.inflate(layoutInflater, parent, false)
        return FinishedOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FinishedOrderViewHolder, position: Int) {
        val currentOrder = finishedOrderList[position]
        holder.bindView(position, currentOrder, onFinishedOrderListener)
    }

    override fun getItemCount(): Int = finishedOrderList.size

    class FinishedOrderViewHolder(val view: ItemFinishedOrderBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bindView(
            position: Int,
            order: OrderDto,
            onFinishedOrderListener: OnFinishedOrderListener
        ) {
            view.position = position
            view.order = order
            view.onFinishedOrderListener = onFinishedOrderListener
            view.executePendingBindings()
        }
    }
}