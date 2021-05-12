package com.app.namllahprovider.presentation.fragments.main.home.new_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.Order
import com.app.namllahprovider.databinding.ItemNewOrderBinding

class NewOrderAdapter(
    private val newOrderList: List<Order>,
    private val onNewOrderListener: OnNewOrderListener
) : RecyclerView.Adapter<NewOrderAdapter.NewOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewOrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNewOrderBinding.inflate(layoutInflater, parent, false)
        return NewOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewOrderViewHolder, position: Int) {
        val currentOrder = newOrderList[position]
        holder.bindView(position, currentOrder, onNewOrderListener)
    }

    override fun getItemCount(): Int = newOrderList.size

    class NewOrderViewHolder(val view: ItemNewOrderBinding) : RecyclerView.ViewHolder(view.root) {
        fun bindView(position: Int, order: Order, onNewOrderListener: OnNewOrderListener) {
            view.position = position
            view.order = order
            view.onNewOrderListener = onNewOrderListener
            view.executePendingBindings()
        }
    }
}