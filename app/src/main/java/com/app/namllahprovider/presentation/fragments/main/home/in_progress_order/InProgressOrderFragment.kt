package com.app.namllahprovider.presentation.fragments.main.home.in_progress_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.FragmentInProgressOrderBinding
import com.app.namllahprovider.domain.utils.OrderStatusRequestType
import com.app.namllahprovider.domain.utils.OrderType
import com.app.namllahprovider.presentation.fragments.main.MainFragmentDirections
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.fragments.main.home.check_timer.CheckTimerFragmentDirections
import com.app.namllahprovider.presentation.fragments.main.home.order_details.OrderDetailsFragmentDirections
import com.app.namllahprovider.presentation.utils.OrderStat
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.getOrderStatus
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InProgressOrderFragment : Fragment(), OnInProgressOrderListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentInProgressOrderBinding: FragmentInProgressOrderBinding? = null

    private var inProgressOrderList = listOf<OrderDto>()

    private var inProgressOrderAdapter: InProgressOrderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentInProgressOrderBinding =
            FragmentInProgressOrderBinding.inflate(inflater, container, false)
        return fragmentInProgressOrderBinding?.apply {
            inProgressOrderLayoutManger =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        inProgressOrderAdapter = InProgressOrderAdapter(requireContext(), inProgressOrderList, this)
        fragmentInProgressOrderBinding?.apply {
            this.inProgressOrderAdapter = this@InProgressOrderFragment.inProgressOrderAdapter
        }
        fetchInProgressOrders()
    }

    private fun observeLiveData() {
        homeViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            Timber.tag(TAG).d("observeLiveData : Loading Status $it")
        })

        homeViewModel.errorLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(activity = requireActivity(), throwable = it)
                it.printStackTrace()
            }
        }

        homeViewModel.noDataLiveData.observe(viewLifecycleOwner) {
            it?.let {

            }
        }

        homeViewModel.changeOrderStatusLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Timber.tag(TAG).d("observeLiveData ChangeOrderStatus : $it")
                Timber.tag(TAG)
                    .d("observeLiveData : ChangeOrderStatus OrderId:${it.order?.id} to ${it.order?.status?.getOrderStatus()}")
                if (it.status) {
                    when (it.order?.status?.getOrderStatus()) {
                        OrderStat.IN_WAY -> {
                            fetchInProgressOrders()
                        }
                        OrderStat.ARRIVE -> {
                            fetchInProgressOrders()
                        }
                        OrderStat.CHECK -> {
                            fetchInProgressOrders()
                        }
                        OrderStat.CANCEL -> {
                            fetchInProgressOrders()
                            SweetAlert.instance.showSuccessAlert(
                                requireActivity(),
                                "Order Canceled Successfully"
                            )
                        }
                        else -> {

                        }
                    }
                } else {
                    homeViewModel.changeErrorMessage(Throwable(it.error))
                }
            }
        }
    }

    private fun fetchInProgressOrders() {
        Timber.tag(TAG).d("fetchInProgressOrders : ")
        homeViewModel.getListOrderRequest(OrderType.IN_PROGRESS)
        homeViewModel.getListOrderLiveData.observe(viewLifecycleOwner, {
            Timber.tag(TAG).d("fetchInProgressOrders : it $it")
            it?.let {
                inProgressOrderList = it

                if (it.isEmpty()) {
                    fragmentInProgressOrderBinding?.llEmptyStatus?.visibility = View.VISIBLE
                }else{
                    fragmentInProgressOrderBinding?.llEmptyStatus?.visibility = View.GONE
                }
                inProgressOrderAdapter?.updateData(inProgressOrderList)
//                homeViewModel.getListOrderLiveData.postValue(null)
            }
        })
    }

    override fun onClickOrder(position: Int) {

    }

    override fun onClickAction(position: Int) {
        val orderDto = inProgressOrderList[position]
        when (orderDto.status?.getOrderStatus()) {
            OrderStat.APPROVED -> {
                homeViewModel.changeOrderStatus(
                    orderId = orderDto.id!!,
                    orderStatusRequestType = OrderStatusRequestType.IN_WAY
                )
            }
            OrderStat.IN_WAY -> {
                homeViewModel.changeOrderStatus(
                    orderId = orderDto.id!!,
                    orderStatusRequestType = OrderStatusRequestType.ARRIVE
                )
            }

            OrderStat.CHECK, OrderStat.WORKING -> {
                // TODO: 6/22/2021 Move to Check Timer
                findNavController().navigate(
                    CheckTimerFragmentDirections.actionGlobalWorkFragment(
                        orderId = orderDto.id!!
                    )
                )
            }
            else -> {
            }
        }
    }

    override fun onClickShowIntMap(position: Int) {
        Timber.tag(TAG).d("onClickShowIntMap : ")
        val orderDto = inProgressOrderList[position]
        findNavController().navigate(
            MainFragmentDirections.actionGlobalMapViewFragment(
                orderId = orderDto.id ?: -1
            )
        )
    }

    override fun onClickCancel(position: Int) {
        Timber.tag(TAG).d("onClickCancel : ")
        val orderDto = inProgressOrderList[position]
        findNavController().navigate(
            OrderDetailsFragmentDirections.actionGlobalCancelReasonsFragment(
                orderId = orderDto.id ?: -1
            )
        )
    }

    companion object {

        private const val TAG = "InProgressOrderFragment"

    }
}