package com.app.namllahprovider.presentation.fragments.main.home.bill

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.BillDto
import com.app.namllahprovider.databinding.ItemBillBinding
import kotlin.collections.ArrayList

class BillsAdapter(var context: Context, var list: ArrayList<BillDto>) :
    RecyclerView.Adapter<BillsAdapter.BillsViewHolder>() {
    class BillsViewHolder(var itemBillsImageBinding: ItemBillBinding) :
        RecyclerView.ViewHolder(itemBillsImageBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder = BillsViewHolder(
        ItemBillBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BillsViewHolder, position: Int){
        holder.itemBillsImageBinding.url = list[position].image.original
        holder.itemBillsImageBinding.visible = false
        holder.itemBillsImageBinding.executePendingBindings()
    }

    fun update(list:ArrayList<BillDto>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}