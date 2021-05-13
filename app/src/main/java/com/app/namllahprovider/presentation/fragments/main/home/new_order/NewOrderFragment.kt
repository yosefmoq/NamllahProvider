package com.app.namllahprovider.presentation.fragments.main.home.new_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.Order
import com.app.namllahprovider.databinding.FragmentNewOrderBinding
import com.app.namllahprovider.presentation.fragments.main.MainFragmentDirections
import com.app.namllahprovider.presentation.fragments.main.home.HomeFragmentDirections
import timber.log.Timber

class NewOrderFragment : Fragment(), OnNewOrderListener {

    private var fragmentNewOrderBinding: FragmentNewOrderBinding? = null

    private val newOrderList = listOf(
        Order(1, "New Order #01"),
        Order(2, "New Order #02"),
        Order(3, "New Order #03"),
        Order(4, "New Order #04"),
        Order(5, "New Order #05"),
        Order(6, "New Order #06"),
        Order(7, "New Order #07"),
        Order(8, "New Order #08"),
        Order(9, "New Order #09"),
        Order(10, "New Order #10"),
        Order(11, "New Order #11"),
        Order(12, "New Order #12"),
        Order(13, "New Order #13"),
        Order(14, "New Order #14"),
        Order(15, "New Order #15"),
        Order(16, "New Order #16"),
        Order(17, "New Order #17"),
        Order(18, "New Order #18"),
        Order(19, "New Order #19"),
        Order(20, "New Order #20"),
    )

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

    companion object {
        private const val TAG = "NewOrderFragment"

        @JvmStatic
        fun newInstance() = NewOrderFragment()
    }

    override fun onClickSeeMore(position: Int) {
        Timber.tag(TAG).d("onClickSeeMore : Click on Item $position")
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToOrderDetailsFragment(
                newOrderList[position].id
            )
        )
    }
}