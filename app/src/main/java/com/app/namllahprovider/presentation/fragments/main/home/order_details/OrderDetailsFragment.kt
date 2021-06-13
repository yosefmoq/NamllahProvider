package com.app.namllahprovider.presentation.fragments.main.home.order_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.FragmentOrderDetailsBinding
import timber.log.Timber

class OrderDetailsFragment : Fragment(), View.OnClickListener {

    private var fragmentOrderDetailsBinding: FragmentOrderDetailsBinding? = null
    private var orderId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = OrderDetailsFragmentArgs.fromBundle(it).orderId
            if (orderId != -1) {
                //Request order details by order id from server
//                fragmentOrderDetailsBinding?.selectedOrder =
//                    OrderDto(id = orderId.toLong(), checkAt = "Fake Title for order $orderId")
            } else {
                Timber.tag(TAG).d("onViewCreated : Can't find order details")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentOrderDetailsBinding =
            FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return fragmentOrderDetailsBinding?.apply {
            actionOnClick = this@OrderDetailsFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = fragmentOrderDetailsBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.order_details)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val TAG = "OrderDetailsFragment"

        @JvmStatic
        fun newInstance() = OrderDetailsFragment()
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentOrderDetailsBinding?.btnAcceptOrder -> onClickAcceptOrder()
        }
    }

    private fun onClickAcceptOrder() {
        /*findNavController().navigate(
            OrderDetailsFragmentDirections.actionOrderDetailsFragmentToOrderBottomSheetFragment(
                name = "Ismail Amassi",
                phoneNumber = "+970 598 123 456 45",
                imageUrl = "https://i.picsum.photos/id/168/200/300.jpg?hmac=ILU5dddz6ohoQEq3_1fmoy2wEFfM1V1JfjLX_JsbOz0"
            )
        )*/
        findNavController().navigate(
            OrderDetailsFragmentDirections.actionOrderDetailsFragmentToWorkItemBottomSheetFragment()
        )
        /*requireActivity().supportFragmentManager.let {
            OrderBottomSheetFragment.newInstance(
                name = "Ismail Amassi",
                phoneNumber = "+970 598 123 456 45",
                imageUrl = "https://i.picsum.photos/id/168/200/300.jpg?hmac=ILU5dddz6ohoQEq3_1fmoy2wEFfM1V1JfjLX_JsbOz0"
            ).apply {
                show(it, tag)
            }
        }*/
    }
}