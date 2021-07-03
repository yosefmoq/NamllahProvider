package com.app.namllahprovider.presentation.fragments.main.home.check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentCheckBinding
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
class CheckFragment : Fragment(), View.OnClickListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentCheckBinding: FragmentCheckBinding? = null
    private var orderId: Int = -1

    private var estimateHoursWork: Int = 0

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
        fragmentCheckBinding = FragmentCheckBinding.inflate(inflater, container, false)
        return fragmentCheckBinding?.apply {
            actionOnClick = this@CheckFragment
            estimateHoursWork = this@CheckFragment.estimateHoursWork
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        observeLiveData()
    }


    private fun initToolbar() {
        val toolbar = fragmentCheckBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.check_up)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }
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
                        OrderStat.CHECK -> {
                            SweetAlert.instance.showSuccessAlert(
                                requireActivity(),
                                "Order Started Successfully"
                            )
                            findNavController().navigate(
                                CheckFragmentDirections.actionCheckFragmentToWorkFragment(
                                    orderId = orderId
                                )
                            )
                        }
                        OrderStat.COMPLETE -> {
                            SweetAlert.instance.showSuccessAlert(
                                requireActivity(),
                                "Order Finished Successfully"
                            )
                            findNavController().popBackStack(R.id.mainFragment, false)
                        }
                        else -> {
                            SweetAlert.instance.showFailAlert(
                                requireActivity(),
                                "Couldn't Start Order"
                            )
                        }
                    }
                } else {
                    homeViewModel.changeErrorMessage(Throwable(it.error))
                }
            }
        }

    }


    companion object {
        private const val TAG = "CheckFragment"
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentCheckBinding?.ibIncreaseNumber -> updateNumber(true)
            fragmentCheckBinding?.ibDecreaseNumber -> updateNumber(false)

            fragmentCheckBinding?.btnStartWork -> onClickStartWork()
            fragmentCheckBinding?.btnCheckingSufficiency -> onClickCheckingSufficiency()
        }
    }

    private fun updateNumber(increaseNumber: Boolean) {
        if (estimateHoursWork == 0 && !increaseNumber) return
        estimateHoursWork =
            if (increaseNumber) estimateHoursWork.plus(1) else estimateHoursWork.minus(1)
        fragmentCheckBinding?.apply {
            estimateHoursWork = this@CheckFragment.estimateHoursWork
        }
    }

    private fun onClickStartWork() {
        homeViewModel.changeOrderStatus(
            orderId = orderId,
            orderStatusRequestType = OrderStatusRequestType.CHECK,
            estimatedTime = estimateHoursWork,
            estimatedPriceParts = 0.0,
            checkDescription = fragmentCheckBinding?.etCheckDescription?.text?.toString() ?: ""
        )
    }

    private fun onClickCheckingSufficiency() {
        homeViewModel.changeOrderStatus(orderId, OrderStatusRequestType.FINISH_ORDER)
    }
}