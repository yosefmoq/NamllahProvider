package com.app.namllahprovider.presentation.fragments.main.home.order_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.namllahprovider.databinding.FragmentOrderBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class OrderBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var fragmentOrderBottomSheetBinding: FragmentOrderBottomSheetBinding? = null

    private var name = ""
    private var phoneNumber = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = OrderBottomSheetFragmentArgs.fromBundle(it).name
            phoneNumber = OrderBottomSheetFragmentArgs.fromBundle(it).phoneNumber
            imageUrl = OrderBottomSheetFragmentArgs.fromBundle(it).imageUrl
//            name = it.getString(ARG_NAME) ?: ""
//            phoneNumber = it.getString(ARG_PHONE_NUMBER) ?: ""
//            imageUrl = it.getString(ARG_IMAGE_URL) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentOrderBottomSheetBinding =
            FragmentOrderBottomSheetBinding.inflate(inflater, container, false)

        return fragmentOrderBottomSheetBinding?.apply {
            actionOnClick = this@OrderBottomSheetFragment
            name = this@OrderBottomSheetFragment.name
            phoneNumber = this@OrderBottomSheetFragment.phoneNumber
            imageUrl = this@OrderBottomSheetFragment.imageUrl
        }?.root
    }

    companion object {
        private const val TAG = "OrderBottomSheetFragmen"

        private const val ARG_NAME = "name"
        private const val ARG_PHONE_NUMBER = "phone_number"
        private const val ARG_IMAGE_URL = "image_url"

        @JvmStatic
        fun newInstance(name: String, phoneNumber: String, imageUrl: String) =
            OrderBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_PHONE_NUMBER, phoneNumber)
                    putString(ARG_IMAGE_URL, imageUrl)
                }
            }

        @JvmStatic
        fun newInstance() = OrderBottomSheetFragment().apply { }
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentOrderBottomSheetBinding?.ibClose -> onClickClose()
            fragmentOrderBottomSheetBinding?.ivCallByPhone -> onClickCallByPhone()
            fragmentOrderBottomSheetBinding?.ivCallByWhatsapp -> onClickCallByWhatsapp()
        }
    }

    private fun onClickClose() {
        dismiss()
    }

    private fun onClickCallByPhone() {

    }

    private fun onClickCallByWhatsapp() {

    }
}