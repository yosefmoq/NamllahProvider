package com.app.namllahprovider.presentation.fragments.main.home.cancel_reasons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentCancelReasonsBinding
import com.app.namllahprovider.domain.utils.OrderStatusRequestType
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.fragments.main.home.order_details.OrderDetailsFragmentArgs
import com.app.namllahprovider.presentation.utils.OrderStat
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import com.app.namllahprovider.presentation.utils.getOrderStatus
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CancelReasonsFragment : Fragment(), View.OnClickListener, OnCheckedChangeListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentCancelReasonsBinding: FragmentCancelReasonsBinding? = null

    private val cancelReasonAdapter = CancelReasonAdapter(this)
    private val cancelReasonList get() = cancelReasonAdapter.getList()
    private val currentSelectPosition get() = cancelReasonAdapter.getCurrentSelectPosition()
    private var orderId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = OrderDetailsFragmentArgs.fromBundle(it).orderId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentCancelReasonsBinding =
            FragmentCancelReasonsBinding.inflate(inflater, container, false)
        return fragmentCancelReasonsBinding?.apply {
            actionOnClick = this@CancelReasonsFragment
            cancelReasonAdapter = this@CancelReasonsFragment.cancelReasonAdapter
        }?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        getCancelReasons()
        observeLiveData()
    }

    private fun observeLiveData() {
        homeViewModel.loadingLiveData.observe(viewLifecycleOwner, {
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

        homeViewModel.errorLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(activity = requireActivity(), throwable = it)
                it.printStackTrace()
            }
        }

        homeViewModel.changeOrderStatusLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Timber.tag(TAG).d("observeLiveData ChangeOrderStatus : $it")
                Timber.tag(TAG)
                    .d("observeLiveData : ChangeOrderStatus OrderId:${it.order?.id} to ${it.order?.status?.getOrderStatus()}")
                if (it.status) {
                    when (it.order?.status?.getOrderStatus()) {
                        OrderStat.CANCEL -> {
                            SweetAlert.instance.showSuccessAlert(
                                requireActivity(),
                                "Order Canceled Successfully"
                            )
                            findNavController().popBackStack(R.id.mainFragment, false)
                        }
                        else -> {
                            SweetAlert.instance.showFailAlert(
                                activity = requireActivity(),
                                message = "Couldn't Cancel Order"
                            )
                        }
                    }
                } else {
                    homeViewModel.changeErrorMessage(Throwable(it.error))
                }
            }
        }

    }


    private fun getCancelReasons() {
        homeViewModel.cancelReasonsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                cancelReasonAdapter.updateData(it)
            }
        }
        homeViewModel.getCancelReasons()
    }

    private fun initToolbar() {
        val toolbar = fragmentCancelReasonsBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.check_up)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }
    }

    companion object {
        private const val TAG = "CancelReasonsFragment"
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentCancelReasonsBinding?.btnCancel -> onClickConfirmCancel()
        }
    }

    private fun onClickConfirmCancel() {
        if (currentSelectPosition == -1){
            SweetAlert.instance.showFailAlert(requireActivity(),"Could not cancel without select reason")
            return
        }

        val cancelReasonDto = cancelReasonList[currentSelectPosition]
        Timber.tag(TAG).d("onClickConfirmCancel : $cancelReasonDto")
        homeViewModel.changeOrderStatus(
            orderId = orderId,
            orderStatusRequestType = OrderStatusRequestType.CANCEL,
            cancelReasonId = cancelReasonDto.id
        )
    }

    override fun onCheckedChange(position: Int) {
        cancelReasonAdapter.updateCurrentSelected(position)
    }
}