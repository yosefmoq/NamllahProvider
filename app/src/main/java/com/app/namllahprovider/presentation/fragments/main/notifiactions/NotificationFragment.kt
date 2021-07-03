package com.app.namllahprovider.presentation.fragments.main.notifiactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.NotificationDto
import com.app.namllahprovider.databinding.FragmentNotificationBinding
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NotificationFragment : Fragment(), OnNotificationListener {

    private val notificationViewModel: NotificationViewModel by viewModels()
    private var fragmentNotificationBinding: FragmentNotificationBinding? = null

    private var notificationList = listOf<NotificationDto>()

    private val notificationAdapter = NotificationAdapter(notificationList, this)

    private var nextPageNumber = 1

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
        observeLiveData()

        fetchNotificationList()

    }

    private fun initToolbar() {
        val toolbar = fragmentNotificationBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.notification)

        toolbar?.root?.navigationIcon = null
    }

    private fun observeLiveData() {
        notificationViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("observeLiveData : Loading Status $it")
                if (it) {
                    SweetAlert.instance.showAlertDialog(
                        context = requireContext(),
                        alertType = SweetAlertType.PROGRESS_TYPE,
                        title = getString(R.string.loading),
                        message = "",
                        confirmText = "",
                        confirmListener = {},
                        cancelText = "",
                        cancelListener = {},
                        cancelable = false,
                    )
                } else {
                    SweetAlert.instance.dismissAlertDialog(true)
                }
            }
        })

        notificationViewModel.errorLiveData.observe(viewLifecycleOwner) {
            it?.let{
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(activity = requireActivity(), throwable = it)
                it.printStackTrace()
            }
        }
    }

    private fun fetchNotificationList() {
        notificationViewModel.getListNotificationRequest(nextPageNumber)
        notificationViewModel.getListNotificationLiveData.observe(viewLifecycleOwner, {
            it?.let {
                nextPageNumber++

                notificationList = it
                notificationAdapter.updateData(notificationList)
                notificationViewModel.getListNotificationLiveData.postValue(null)
            }
        })
    }

    override fun onClickNotification(position: Int) {

    }

    companion object {
        private const val TAG = "NotificationFragment"
        fun newInstance() = NotificationFragment()
    }
}