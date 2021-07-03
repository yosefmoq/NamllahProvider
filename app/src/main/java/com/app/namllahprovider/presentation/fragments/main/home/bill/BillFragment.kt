package com.app.namllahprovider.presentation.fragments.main.home.bill

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.FragmentBillBinding
import com.app.namllahprovider.domain.utils.OrderStatusRequestType
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.fragments.main.home.order_details.OrderDetailsFragmentArgs
import com.app.namllahprovider.presentation.utils.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.io.File


@AndroidEntryPoint
class BillFragment : Fragment(), View.OnClickListener, BillListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentBillBinding: FragmentBillBinding? = null
    private var orderId: Int = -1
    private var deliveryTimes: Int = 0

    private val SELECT_PHOTO: Int = 143


    private var orderDto: OrderDto? = null
    private val billAdapter = BillAdapter(this)
    private val billList get() = billAdapter.getBillList()
    private val billListBody = mutableListOf<MultipartBody.Part>()

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
        fragmentBillBinding = FragmentBillBinding.inflate(inflater, container, false)
        return fragmentBillBinding?.apply {
            actionOnClick = this@BillFragment
            billAdapter = this@BillFragment.billAdapter
            deliveryTimes = this@BillFragment.deliveryTimes
            rvBills.layoutManager =
                object : LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false) {
                    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                        lp!!.width = width / 3
                        return true
                    }
                }
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        getOrderData()
        observeLiveData()
    }

    private fun initToolbar() {
        val toolbar = fragmentBillBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.bill)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getOrderData() {
        if (orderId != -1) {
            homeViewModel.getOrderData(orderId)
            homeViewModel.getOrderDataLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    orderDto = it
                    fragmentBillBinding?.apply {
                        orderDto = this@BillFragment.orderDto
                        if (orderDto?.status?.getOrderStatus() != OrderStat.WORKING) {
                            Toast.makeText(
                                requireContext(),
                                "Can't finish this order",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack(R.id.mainFragment, false)
                        }
                    }
                }
            }
        } else {
            Timber.tag(TAG).d("onViewCreated : Can't find order details")
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
                    orderDto = it.order
                    fragmentBillBinding?.apply {
                        orderDto = this@BillFragment.orderDto
                    }
                    when (it.order?.status?.getOrderStatus()) {
                        OrderStat.COMPLETE -> {
                            SweetAlert.instance.showSuccessAlert(
                                requireActivity(),
                                "Order Finished Successfully"
                            )
                            findNavController().navigate(R.id.mainFragment)
                        }
                        else -> {

                        }
                    }
                } else {
                    homeViewModel.changeErrorMessage(Throwable(it.msg))
                }
            }
        }
    }

    companion object {
        private const val TAG = "BillFragment"
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentBillBinding?.btnCheckOut -> onClickCheckout()
            fragmentBillBinding?.btnUploadBill -> onClickUploadImage()
            fragmentBillBinding?.ibIncreaseNumber -> updateDeliveryTimes(true)
            fragmentBillBinding?.ibDecreaseNumber -> updateDeliveryTimes(false)
        }
    }

    private fun onClickCheckout() {
        val boughtPrice =
            fragmentBillBinding?.etTotalItemsPrice?.text?.toString()?.toIntOrNull() ?: 0
        val boughtPriceBody: RequestBody = boughtPrice.toString().toRequestBody(MultipartBody.FORM)
        val deliveryTimesBody: RequestBody =
            deliveryTimes.toString().toRequestBody(MultipartBody.FORM)

        homeViewModel.changeOrderStatus(
            orderId = orderId,
            orderStatusRequestType = OrderStatusRequestType.ADD_BILLS,
            boughtPrice = boughtPriceBody,
            bringTimes = deliveryTimesBody,
            bills = billListBody,
        )
    }

    private fun onClickUploadImage() {
        Timber.tag(TAG).d("onClickChangeImage : ")
        if (hasStoragePermission()) {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            someActivityResultLauncher.launch(intent)
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.take_image),
                SELECT_PHOTO,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun hasStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private val someActivityResultLauncher: ActivityResultLauncher<Intent?> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent = result.data!!
                billAdapter.addBillToList(data.data!!)

                val realPath: String = FileUtils().getPath(requireContext(), data.data!!)!!
                val file = File(realPath)
                val requestBody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())

                val body: MultipartBody.Part =
                    MultipartBody.Part.createFormData("bills[]", file.name, requestBody)
                billListBody.add(body)
            }
        }

    private fun updateDeliveryTimes(increaseNumber: Boolean) {
        if (deliveryTimes == 0 && !increaseNumber) return
        deliveryTimes = if (increaseNumber) deliveryTimes.plus(1) else deliveryTimes.minus(1)
        fragmentBillBinding?.apply {
            deliveryTimes = this@BillFragment.deliveryTimes
        }
    }

    override fun onClickBill(position: Int) {

    }

    override fun onClickDeleteBill(position: Int) {
        billAdapter.removeBillFromList(position)

    }
}