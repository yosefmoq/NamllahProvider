package com.app.namllahprovider.presentation.fragments.main.home.finished_order

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
import com.app.namllahprovider.databinding.FragmentFinishedOrderBinding
import com.app.namllahprovider.domain.utils.OrderType
import com.app.namllahprovider.presentation.fragments.main.MainFragmentDirections
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.utils.OrderStat.*
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.getOrderStatus
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FinishedOrderFragment : Fragment(), OnFinishedOrderListener {
    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentFinishedOrderBinding: FragmentFinishedOrderBinding? = null

    private var finishedOrderList = listOf<OrderDto>()

    private val finishedOrderAdapter = FinishedOrderAdapter(finishedOrderList, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentFinishedOrderBinding =
            FragmentFinishedOrderBinding.inflate(inflater, container, false)
        return fragmentFinishedOrderBinding?.apply {
            finishedOrderAdapter = this@FinishedOrderFragment.finishedOrderAdapter
            finishedOrderLayoutManger =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()

        fetchFinishedOrders()
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
    }

    private fun fetchFinishedOrders() {
        Timber.tag(TAG).d("fetchFinishedOrders : ")
        homeViewModel.getListOrderRequest(OrderType.FINISHED)
        homeViewModel.getListOrderLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("fetchFinishedOrders : it $it")
                finishedOrderList = it
                if (it.isEmpty()) {
                    fragmentFinishedOrderBinding?.llEmptyStatus?.visibility = View.VISIBLE
                } else {
                    fragmentFinishedOrderBinding?.llEmptyStatus?.visibility = View.GONE
                }
                finishedOrderAdapter.updateData(finishedOrderList)
                homeViewModel.getListOrderLiveData.postValue(null)
            }
        })
    }

    override fun onClickOrder(position: Int) {
        val order = finishedOrderList[position]
        Timber.tag(TAG).d("onClickOrde : $order")
        when (order.status?.getOrderStatus()) {
            COMPLETE -> {
                //Case 1 when When order.payment is empty this mean customer not selected payment method (Show message "Wait until customer pay")
                //Case 2 when When order.payment is full we will check isPayComplete if 0 that's mean provider have to confirm it("Show Button to confirm it"send pay request check postman")
                //Case 3 when When order.payment is full we will check isPayComplete if 1 that's mean order PAID and CLOSED(Show message to user "Order has closed")

                findNavController().navigate(
                    MainFragmentDirections.actionGlobalBillFragment(
                        finishedOrderList[position].id?: 0
                    )
                )

            }
            CANCEL -> {
                Timber.tag(TAG).d("onClickOrder : Order is Canceled")
            }
            else -> {
                Timber.tag(TAG).d("onClickOrder : Order Not Supported")
            }
        }

    }

    companion object {
        private const val TAG = "FinishedOrderFragment"

    }
}