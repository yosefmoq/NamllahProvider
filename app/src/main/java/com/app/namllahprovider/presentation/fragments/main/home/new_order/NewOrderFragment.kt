package com.app.namllahprovider.presentation.fragments.main.home.new_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.FragmentNewOrderBinding
import com.app.namllahprovider.domain.utils.OrderType
import com.app.namllahprovider.presentation.fragments.main.MainFragmentDirections
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.utils.SweetAlert
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NewOrderFragment : Fragment(), OnNewOrderListener {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private var fragmentNewOrderBinding: FragmentNewOrderBinding? = null


    private var newOrderList = listOf<OrderDto>()

    private val newOrderAdapter = NewOrderAdapter(newOrderList, this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNewOrderBinding = FragmentNewOrderBinding.inflate(inflater, container, false)
        return fragmentNewOrderBinding?.apply {
            newOrderAdapter = this@NewOrderFragment.newOrderAdapter
            newOrderLayoutManger =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()

        fetchNewOrders()
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

    private fun fetchNewOrders() {
        Timber.tag(TAG).d("fetchNewOrder : ")
        homeViewModel.getListOrderRequest(OrderType.NEW)
        homeViewModel.getListOrderLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("fetchNewOrder : it $it")
                if (it.isEmpty()) {
                    fragmentNewOrderBinding?.llEmptyStatus?.visibility = View.VISIBLE
                }else{
                    fragmentNewOrderBinding?.llEmptyStatus?.visibility = View.GONE
                }
                newOrderList = it
                newOrderAdapter.updateData(newOrderList)
                homeViewModel.getListOrderLiveData.postValue(null)
            }
        })
    }


    companion object {
        private const val TAG = "NewOrderFragment"

    }

    override fun onClickSeeMore(position: Int) {
        Timber.tag(TAG).d("onClickSeeMore : Click on Item $position")
        findNavController().navigate(
            MainFragmentDirections.actionHomeFragmentToOrderDetailsFragment(
                newOrderList[position].id?.toInt() ?: 0
            )
        )
    }
}