package com.app.namllahprovider.presentation.fragments.main.home.in_progress_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.FragmentInProgressOrderBinding
import com.app.namllahprovider.domain.utils.OrderType
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.fragments.wizard.sign_up.SignUpFragment
import com.app.namllahprovider.presentation.utils.SweetAlert
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InProgressOrderFragment : Fragment(), OnInProgressOrderListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentInProgressOrderBinding: FragmentInProgressOrderBinding? = null

    private var inProgressOrderList  = listOf<OrderDto>()

    private val inProgressOrderAdapter = InProgressOrderAdapter(inProgressOrderList, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentInProgressOrderBinding =
            FragmentInProgressOrderBinding.inflate(inflater, container, false)
        return fragmentInProgressOrderBinding?.apply {
            inProgressOrderAdapter = this@InProgressOrderFragment.inProgressOrderAdapter
            inProgressOrderLayoutManger =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()

        fetchInProgressOrders()
    }

    private fun observeLiveData() {
        homeViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            Timber.tag(TAG).d("observeLiveData : Loading Status $it")
        })

        homeViewModel.errorLiveData.observe(viewLifecycleOwner) {
            it?.let{
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(activity = requireActivity(), throwable = it)
                it.printStackTrace()
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
                inProgressOrderAdapter.updateData(inProgressOrderList)
                homeViewModel.getListOrderLiveData.postValue(null)
            }
        })
    }

    override fun onClickOrder(position: Int) {

    }

    companion object {

        private const val TAG = "InProgressOrderFragment"

        @JvmStatic
        fun newInstance() = InProgressOrderFragment()
    }
}