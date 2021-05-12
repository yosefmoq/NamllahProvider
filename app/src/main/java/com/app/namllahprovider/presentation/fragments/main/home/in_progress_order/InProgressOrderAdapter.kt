package com.app.namllahprovider.presentation.fragments.main.home.in_progress_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.Order
import com.app.namllahprovider.databinding.ItemInProgressOrderBinding

class InProgressOrderAdapter(
    private val newOrderList: List<Order>,
    private val onInProgressOrderListener: OnInProgressOrderListener
) : RecyclerView.Adapter<InProgressOrderAdapter.NewOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewOrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInProgressOrderBinding.inflate(layoutInflater, parent, false)
        return NewOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewOrderViewHolder, position: Int) {
        val currentOrder = newOrderList[position]
        holder.bindView(position, currentOrder, onInProgressOrderListener)
    }

    override fun getItemCount(): Int = newOrderList.size

    class NewOrderViewHolder(val view: ItemInProgressOrderBinding) : RecyclerView.ViewHolder(view.root) {
        fun bindView(position: Int, order: Order, onInProgressOrderListener: OnInProgressOrderListener) {
            view.position = position
            view.order = order
            view.onInProgressOrderListener = onInProgressOrderListener
            view.executePendingBindings()
        }
    }
}