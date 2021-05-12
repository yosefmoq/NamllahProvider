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
import com.app.namllahprovider.data.model.Notification
import com.app.namllahprovider.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment(), OnNotificationListener {

    private var fragmentNotificationBinding: FragmentNotificationBinding? = null

    private val notificationList = listOf(
        Notification(id = 1, name = "Ali Ahmad", description = "Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, Notification Description #01, "),
        Notification(id = 2, name = "Ali Ahmad", description = "Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, Notification Description #02, "),
        Notification(id = 3, name = "Ali Ahmad", description = "Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, Notification Description #03, "),
        Notification(id = 4, name = "Ali Ahmad", description = "Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, Notification Description #04, "),
        Notification(id = 5, name = "Ali Ahmad", description = "Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, Notification Description #05, "),
        Notification(id = 6, name = "Ali Ahmad", description = "Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, Notification Description #06, "),
        Notification(id = 7, name = "Ali Ahmad", description = "Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, Notification Description #07, "),
        Notification(id = 8, name = "Ali Ahmad", description = "Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, Notification Description #08, "),
        Notification(id = 9, name = "Ali Ahmad", description = "Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, Notification Description #09, "),
        Notification(id = 10, name = "Ali Ahmad", description = "Notification Description #10, Notification Description #10"),
        Notification(id = 11, name = "Ali Ahmad", description = "Notification Description #11, Notification Description #11"),
        Notification(id = 12, name = "Ali Ahmad", description = "Notification Description #12, Notification Description #12"),
        Notification(id = 13, name = "Ali Ahmad", description = "Notification Description #13, Notification Description #13"),
        Notification(id = 14, name = "Ali Ahmad", description = "Notification Description #14, Notification Description #14"),
        Notification(id = 15, name = "Ali Ahmad", description = "Notification Description #15, Notification Description #15"),
        Notification(id = 16, name = "Ali Ahmad", description = "Notification Description #16, Notification Description #16"),
        Notification(id = 17, name = "Ali Ahmad", description = "Notification Description #17, Notification Description #17"),
        Notification(id = 18, name = "Ali Ahmad", description = "Notification Description #18, Notification Description #18"),
        Notification(id = 19, name = "Ali Ahmad", description = "Notification Description #19, Notification Description #19"),
        Notification(id = 20, name = "Ali Ahmad", description = "Notification Description #20, Notification Description #20"),
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
        fun getInstance() = NotificationFragment()

    }
}