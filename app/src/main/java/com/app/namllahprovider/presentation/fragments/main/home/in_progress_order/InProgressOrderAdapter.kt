package com.app.namllahprovider.presentation.fragments.main.home.in_progress_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.ItemInProgressOrderBinding
import com.app.namllahprovider.domain.utils.OrderStatusRequestType
import com.app.namllahprovider.presentation.utils.OrderStat
import com.app.namllahprovider.presentation.utils.getOrderStatus

class InProgressOrderAdapter(
    private var newOrderList: List<OrderDto>,
    private val onInProgressOrderListener: OnInProgressOrderListener
) : RecyclerView.Adapter<InProgressOrderAdapter.NewOrderViewHolder>() {

    fun updateData(newOrderList: List<OrderDto>) {
        this.newOrderList = newOrderList
        notifyDataSetChanged()
    }

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

    class NewOrderViewHolder(val view: ItemInProgressOrderBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bindView(
            position: Int,
            order: OrderDto,
            onInProgressOrderListener: OnInProgressOrderListener
        ) {
            var buttonVisibilitySetterAction =  true
            view.position = position
            view.order = order
            view.orderActionText = when (order.status?.getOrderStatus()) {
                OrderStat.APPROVED -> "In way"
                OrderStat.IN_WAY -> "Arrive"
                OrderStat.CHECK  -> "Work Now"
                OrderStat.WORKING  -> "Continue"
                else -> {
                    buttonVisibilitySetterAction = false
                    "gone"
                }
            }

            view.onInProgressOrderListener = onInProgressOrderListener
            view.buttonVisibilitySetterAction = buttonVisibilitySetterAction
            view.executePendingBindings()
        }
    }
}