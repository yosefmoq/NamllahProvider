package com.app.namllahprovider.presentation.base

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.R
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



}