package com.app.namllahprovider.presentation.fragments.main.notifiactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.NotificationDto
import com.app.namllahprovider.databinding.ItemNotificationBinding

//String formattedText = "This <i>is</i> a <b>test</b> of <a href='http://foo.com'>html</a>";
class NotificationAdapter(
    var notificationList: List<NotificationDto>,
    var onNotificationListener: OnNotificationListener
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    fun updateData(notificationList: List<NotificationDto>) {
        this.notificationList = notificationList
        notifyDataSetChanged()
    }

    class NotificationViewHolder(val view: ItemNotificationBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bindView(
            position: Int,
            notificationDto: NotificationDto,
            onNotificationListener: OnNotificationListener
        ) {
            view.position = position
            view.notification = notificationDto
            view.onNotificationListener = onNotificationListener
            view.executePendingBindings()
            view.tvNotificationText.text = "${notificationDto.data.msg.ar}" /*HtmlCompat.fromHtml(
                "<b>${notificationDto.id}</b> ${notificationDto.data.msg.ar}",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationBinding.inflate(layoutInflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val currentNotification = notificationList[position]
        holder.bindView(position, currentNotification, onNotificationListener)
    }

    override fun getItemCount(): Int = notificationList.size
}