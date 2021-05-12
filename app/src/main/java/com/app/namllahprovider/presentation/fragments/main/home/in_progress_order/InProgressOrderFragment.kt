package com.app.namllahprovider.presentation.fragments.main.home.in_progress_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.Order
import com.app.namllahprovider.databinding.FragmentInProgressOrderBinding


class InProgressOrderFragment : Fragment(), OnInProgressOrderListener {

    private var fragmentInProgressOrderBinding: FragmentInProgressOrderBinding? = null

    private val inProgressOrderList = listOf(
        Order(1, "In Progress Order #01"),
        Order(2, "In Progress Order #02"),
        Order(3, "In Progress Order #03"),
        Order(4, "In Progress Order #04"),
        Order(5, "In Progress Order #05"),
        Order(6, "In Progress Order #06"),
        Order(7, "In Progress Order #07"),
        Order(8, "In Progress Order #08"),
        Order(9, "In Progress Order #09"),
        Order(10, "In Progress Order #10"),
        Order(11, "In Progress Order #11"),
        Order(12, "In Progress Order #12"),
        Order(13, "In Progress Order #13"),
        Order(14, "In Progress Order #14"),
        Order(15, "In Progress Order #15"),
        Order(16, "In Progress Order #16"),
        Order(17, "In Progress Order #17"),
        Order(18, "In Progress Order #18"),
        Order(19, "In Progress Order #19"),
        Order(20, "In Progress Order #20"),
    )

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

    override fun onClickOrder(position: Int) {

    }

    companion object {

        private const val TAG = "InProgressOrderFragment"

        @JvmStatic
        fun newInstance() = InProgressOrderFragment()
    }
}