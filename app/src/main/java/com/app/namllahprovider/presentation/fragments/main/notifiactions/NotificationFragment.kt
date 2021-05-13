package com.app.namllahprovider.presentation.fragments.main.notifiactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.NotificationDto
import com.app.namllahprovider.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment(), OnNotificationListener {

    private var fragmentNotificationBinding: FragmentNotificationBinding? = null

    private val notificationList = listOf(
        NotificationDto(id = 1, name = "Ali Ahmad", description = "Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, "),
        NotificationDto(id = 2, name = "Ali Ahmad", description = "Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, "),
        NotificationDto(id = 3, name = "Ali Ahmad", description = "Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, "),
        NotificationDto(id = 4, name = "Ali Ahmad", description = "Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, "),
        NotificationDto(id = 5, name = "Ali Ahmad", description = "Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, "),
        NotificationDto(id = 6, name = "Ali Ahmad", description = "Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, "),
        NotificationDto(id = 7, name = "Ali Ahmad", description = "Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, "),
        NotificationDto(id = 8, name = "Ali Ahmad", description = "Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, "),
        NotificationDto(id = 9, name = "Ali Ahmad", description = "Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, "),
        NotificationDto(id = 10, name = "Ali Ahmad", description = "Notification Description #10, Notification Description #10"),
        NotificationDto(id = 11, name = "Ali Ahmad", description = "Notification Description #11, Notification Description #11"),
        NotificationDto(id = 12, name = "Ali Ahmad", description = "Notification Description #12, Notification Description #12"),
        NotificationDto(id = 13, name = "Ali Ahmad", description = "Notification Description #13, Notification Description #13"),
        NotificationDto(id = 14, name = "Ali Ahmad", description = "Notification Description #14, Notification Description #14"),
        NotificationDto(id = 15, name = "Ali Ahmad", description = "Notification Description #15, Notification Description #15"),
        NotificationDto(id = 16, name = "Ali Ahmad", description = "Notification Description #16, Notification Description #16"),
        NotificationDto(id = 17, name = "Ali Ahmad", description = "Notification Description #17, Notification Description #17"),
        NotificationDto(id = 18, name = "Ali Ahmad", description = "Notification Description #18, Notification Description #18"),
        NotificationDto(id = 19, name = "Ali Ahmad", description = "Notification Description #19, Notification Description #19"),
        NotificationDto(id = 20, name = "Ali Ahmad", description = "Notification Description #20, Notification Description #20"),
    )

    private val notificationAdapter = NotificationAdapter(notificationList, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentNotificationBinding =
            FragmentNotificationBinding.inflate(inflater, container, false)
        return fragmentNotificationBinding?.apply {
            notificationAdapter = this@NotificationFragment.notificationAdapter
            notificationManger =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = fragmentNotificationBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.notification)

        toolbar?.root?.navigationIcon = null
    }

    override fun onClickNotification(position: Int) {

    }

    companion object {
        private const val TAG = "NotificationFragment"
        fun newInstance() = NotificationFragment()

    }
}