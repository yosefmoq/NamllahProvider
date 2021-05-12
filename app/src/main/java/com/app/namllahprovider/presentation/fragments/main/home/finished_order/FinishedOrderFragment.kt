package com.app.namllahprovider.presentation.fragments.main.home.finished_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.data.model.Order
import com.app.namllahprovider.databinding.FragmentFinishedOrderBinding

class FinishedOrderFragment : Fragment(), OnFinishedOrderListener {

    private var fragmentFinishedOrderBinding: FragmentFinishedOrderBinding? = null

    private val finishedOrderList = listOf(
        Order(1, "Finished Order #01"),
        Order(2, "Finished Order #02"),
        Order(3, "Finished Order #03"),
        Order(4, "Finished Order #04"),
        Order(5, "Finished Order #05"),
        Order(6, "Finished Order #06"),
        Order(7, "Finished Order #07"),
        Order(8, "Finished Order #08"),
        Order(9, "Finished Order #09"),
        Order(10, "Finished Order #10"),
        Order(11, "Finished Order #11"),
        Order(12, "Finished Order #12"),
        Order(13, "Finished Order #13"),
        Order(14, "Finished Order #14"),
        Order(15, "Finished Order #15"),
        Order(16, "Finished Order #16"),
        Order(17, "Finished Order #17"),
        Order(18, "Finished Order #18"),
        Order(19, "Finished Order #19"),
        Order(20, "Finished Order #20"),
    )

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

    override fun onClickOrder(position: Int) {

    }

    companion object {
        private const val TAG = "FinishedOrderFragment"

        @JvmStatic
        fun newInstance() = FinishedOrderFragment()
    }
}