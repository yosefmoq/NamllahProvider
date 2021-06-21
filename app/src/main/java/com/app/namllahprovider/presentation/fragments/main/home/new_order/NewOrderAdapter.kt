package com.app.namllahprovider.presentation.fragments.main.home.new_order

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.ItemNewOrderBinding
import com.app.namllahprovider.presentation.utils.getAddressFromLatAndLng

class NewOrderAdapter(
    private var newOrderList: List<OrderDto>,
    private val onNewOrderListener: OnNewOrderListener
) : RecyclerView.Adapter<NewOrderAdapter.NewOrderViewHolder>() {

    fun updateData(newOrderList: List<OrderDto>) {
        this.newOrderList = newOrderList
        notifyDataSetChanged()
    }

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
        val context = view.root.context
        fun bindView(position: Int, order: OrderDto, onNewOrderListener: OnNewOrderListener) {
            val address = getAddressFromLatAndLng(context,order.lat?:0.0 , order.lng?:0.0)
            view.position = position
            view.order = order
            view.orderAddress = address
            view.onNewOrderListener = onNewOrderListener
            view.executePendingBindings()
        }

    }
}