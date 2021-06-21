package com.app.namllahprovider.presentation.fragments.main.home.order_details

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.FragmentOrderDetailsBinding
import com.app.namllahprovider.domain.utils.OrderStatusRequestType
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OrderDetailsFragment : Fragment(), View.OnClickListener {
    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentOrderDetailsBinding: FragmentOrderDetailsBinding? = null
    private var orderId: Int = -1
    private var orderDto: OrderDto? = null
    private var address: String = ""

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    val MY_PERMISSIONS_REQUEST_LOCATION = 1234
    lateinit var googleMap: GoogleMap
    var lat: Double = 0.0
    var lng: Double = 0.0

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.clear()
/*        googleMap.setOnMapClickListener {
            googleMap.clear()
            lat = it.latitude
            lng = it.longitude
            val sydney = LatLng(lat, lng)
            googleMap.addMarker(MarkerOptions().position(sydney).title("My Location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))

        }*/
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true

        getLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = OrderDetailsFragmentArgs.fromBundle(it).orderId
        }
    }

    private fun getOrderData() {
        if (orderId != -1) {
            homeViewModel.getOrderData(orderId)
            homeViewModel.getOrderDataLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    fragmentOrderDetailsBinding?.apply {
                        this@OrderDetailsFragment.address =
                            getAddressFromLatAndLng(requireContext(), it.lat ?: 0.0, it.lng ?: 0.0)
                        this.orderDto = it
                        this@OrderDetailsFragment.orderDto = it
                        this.address = this@OrderDetailsFragment.address
                        getLocation()
                    }
//                    fragmentOrderDetailsBinding.mvOrderDetails.

                }
            }
        } else {
            Timber.tag(TAG).d("onViewCreated : Can't find order details")
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
            fusedLocationProvider =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            checkLocationPermission()
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        getOrderData()
        observeLiveData()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun observeLiveData() {
        homeViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("observeLiveData : Loading Status $it")
                if (it) {
                    SweetAlert.instance.showAlertDialog(
                        context = requireContext(),
                        alertType = SweetAlertType.PROGRESS_TYPE,
                        title = "Loading",
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
                    when (it.order?.status?.getOrderStatus()) {
                        OrderStat.APPROVED -> {
                            SweetAlert.instance.showSuccessAlert(
                                requireActivity(),
                                "Order Approved Successfully"
                            )
                            findNavController().popBackStack()
                        }
                        OrderStat.CANCEL -> {
                            SweetAlert.instance.showSuccessAlert(
                                requireActivity(),
                                "Order Canceled Successfully"
                            )
                            // TODO: 6/20/2021 OPEN CANCEL REASONS UIs
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

    private fun checkLocationPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                requestLocationPermission()
            } else {
                requestLocationPermission()
            }
            false
        } else {
            true
        }
    }


    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }

    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider!!.lastLocation.addOnCompleteListener {
                lat = orderDto?.lat ?: 0.0
                lng = orderDto?.lng ?: 0.0
                val myLocation = LatLng(lat, lng)
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(myLocation).title(address))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13f))
            }

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
            fragmentOrderDetailsBinding?.btnDeclineOrder -> onClickDeclineOrder()
        }
    }

    private fun onClickAcceptOrder() {
        homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.APPROVED)
    }

    private fun onClickDeclineOrder() {
//        homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.CANCEL)
        // TODO: 6/20/2021 MOVE TO CANCEL REASON

    }
}