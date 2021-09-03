package com.app.namllahprovider.presentation.fragments.main.home.bill

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.databinding.ItemBillBinding

class BillAdapter constructor(
    private val billListener: BillListener
) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {

    private var billList = mutableListOf<Uri>()

    fun getBillList() = billList
    fun updateList(billList: MutableList<Uri>) {
        this.billList = billList
        notifyDataSetChanged()
    }

    fun addBillToList(billDto: Uri) {
        val newIndex = billList.size
        billList.add(newIndex, billDto)
        notifyItemInserted(newIndex)
    }

    fun removeBillFromList(position: Int) {

        if (position < this.billList.size) {
            this.billList.removeAt(position)
            notifyItemRemoved(position)
            for (index in this.billList.indices) {
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBillBinding.inflate(inflater,parent,false)
        return BillViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val currentBill = billList[position]
        holder.bindView(
            uri = currentBill,
            billListener = billListener,
            position = position
        )
    }

    override fun getItemCount(): Int = billList.size

    class BillViewHolder(val item: ItemBillBinding) : RecyclerView.ViewHolder(item.root) {
        fun bindView(
            uri: Uri,
            billListener: BillListener,
            position: Int
        ) {
            item.apply {
                this.billListener = billListener
                this.position = position
            }
            item.ivBillImage.setImageURI(uri)
        }
    }
}