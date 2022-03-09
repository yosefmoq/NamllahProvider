package com.app.namllahprovider.presentation.base

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.OrderDto
import com.bumptech.glide.Glide

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["app:setAdapter"])
    fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>?) {
        this.run { this.adapter = adapter }
    }

    @JvmStatic
    @BindingAdapter(value = ["imageUrl"])
    fun ImageView.loadImage(url: String?) {
        if (!url.isNullOrEmpty() && url.contains("http")) {
            if (!url.isNullOrEmpty()) {
                Glide.with(this.context)
                    .load(url)
                    .error(R.drawable.ic_blue_logo)
                    .placeholder(R.drawable.person)
                    .into(this)
            }
        }else{
            this.setImageResource(R.drawable.person)
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["orderStatus"])
    fun TextView.loadBackground(order:OrderDto) {
        if(order.status!!.id == 7){
            if(order.isPayComplete == 1){
                this.background = ContextCompat.getDrawable(this.context,R.drawable.bg_orders_status_green)
            }else{
                this.background = ContextCompat.getDrawable(this.context,R.drawable.bg_order_status_gray)
            }
        }else if(order.status!!.id == 8){
            this.background = ContextCompat.getDrawable(this.context,R.drawable.bg_order_status_red)

        }
    }







}