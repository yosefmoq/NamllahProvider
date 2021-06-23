package com.app.namllahprovider.presentation.fragments.main.profile.services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.ServiceDto
import com.app.namllahprovider.databinding.ItemUserServiceBinding
import timber.log.Timber

class UserServiceAdapter(

) : RecyclerView.Adapter<UserServiceAdapter.UserServiceViewHolder>() {

    var userServicesList = listOf<ServiceDto>()

    fun updateData(servicesList: List<ServiceDto>) {
        this.userServicesList = servicesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserServiceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserServiceBinding.inflate(inflater,parent,false)
        return UserServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserServiceViewHolder, position: Int) {
        val currentService = userServicesList[position]
        Timber.tag("TAG").d("onBindViewHolder : currentService $currentService")
        holder.bindView(currentService, position)
    }

    override fun getItemCount(): Int = userServicesList.size

    class UserServiceViewHolder(val view: ItemUserServiceBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bindView(serviceDto: ServiceDto, position: Int) {
            view.userServiceDto = serviceDto
            view.position = position
        }

    }

}