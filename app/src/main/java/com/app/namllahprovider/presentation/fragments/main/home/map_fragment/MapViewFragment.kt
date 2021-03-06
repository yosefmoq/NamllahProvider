package com.app.namllahprovider.presentation.fragments.main.home.map_fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.FragmentMapViewBinding
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
import android.widget.Toast

import android.content.Intent
import android.net.Uri
import android.util.Log
import java.lang.NullPointerException
import java.util.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MapViewFragment : Fragment(), View.OnClickListener {
    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentMapViewBinding: FragmentMapViewBinding? = null
    private var orderId: Int = -1
    private var orderDto: OrderDto? = null
    private var address: String = ""

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    val MY_PERMISSIONS_REQUEST_LOCATION = 1234
    lateinit var googleMap: GoogleMap
    var lat: Double = 0.0
    var lng: Double = 0.0

    private val callback = OnMapReadyCallback { googleMap ->
        Timber.tag(TAG).d("googleMap : callback")
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
        googleMap.setPadding(0, 0, 50, 450)
        getLocation()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = MapViewFragmentArgs.fromBundle(it).orderId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMapViewBinding = FragmentMapViewBinding.inflate(inflater, container, false)
        return fragmentMapViewBinding?.apply {
            actionOnClick = this@MapViewFragment
            fusedLocationProvider =
                LocationServices.getFusedLocationProviderClient(requireActivity())
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

    private fun initToolbar() {
        val toolbar = fragmentMapViewBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.client_location)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getOrderData() {
        if (orderId != -1) {
            homeViewModel.getOrderData(orderId)
            homeViewModel.getOrderDataLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    fragmentMapViewBinding?.apply {
                        this@MapViewFragment.address =
                            getAddressFromLatAndLng(requireContext(), it.lat ?: 0.0, it.lng ?: 0.0)
                        this.orderDto = it
                        this@MapViewFragment.orderDto = it
                        lat = orderDto?.lat ?: 0.0
                        lng = orderDto?.lng ?: 0.0
                        val myLocation = LatLng(lat, lng)
                        googleMap.clear()
                        googleMap.addMarker(MarkerOptions().position(myLocation).title(address))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
                        this.address = this@MapViewFragment.address
                        if (it.status?.getOrderStatus()!! >= OrderStat.ARRIVE) {
                            llMapBtns.visibility = View.VISIBLE

                            if (it.arrive_at!!.isNotEmpty()) {

                                val hours = it.arrive_at!!.split(" ")[1].split(":")[0].toInt()
                                val minutes = it.arrive_at!!.split(" ")[1].split(":")[1].toInt()

                                val calender = Calendar.getInstance()
                                val nowHours = calender.get(Calendar.HOUR_OF_DAY)
                                val nowMinutes = calender.get(Calendar.MINUTE)

                                if (nowHours.minus(hours) == 0) {
                                    if (nowMinutes - minutes >= 10) {
                                        fragmentMapViewBinding!!.btnClientNotInLocation.visibility =
                                            View.VISIBLE
                                    } else {
                                        fragmentMapViewBinding!!.btnClientNotInLocation.visibility =
                                            View.GONE
                                    }
                                } else {
                                    fragmentMapViewBinding!!.btnClientNotInLocation.visibility =
                                        View.VISIBLE
                                }
                            }

                        } else {
                            llMapBtns.visibility = View.GONE
                        }
                        getLocation()
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
                    when (it.order?.status?.getOrderStatus()) {
                        OrderStat.CHECK -> {
                            findNavController().navigate(
                                MapViewFragmentDirections.actionMapViewFragmentToCheckFragment(
                                    orderId = orderId
                                )
                            )
                        }
                        OrderStat.CANCEL -> {
                            SweetAlert.instance.showSuccessAlert(
                                requireActivity(),
                                "Order Canceled Successfully"
                            )
                            findNavController().popBackStack(R.id.mainFragment, false)
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

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider!!.lastLocation.addOnCompleteListener {
            }

        }
    }

    companion object {
        private const val TAG = "MapViewFragment"
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentMapViewBinding?.btnStartChecking -> onClickStartChecking()
            fragmentMapViewBinding?.btnClientNotInLocation -> onClickDeclineOrder()
            fragmentMapViewBinding?.btnDirections -> {
                val latitude: String = java.lang.String.valueOf(lat)
                val longitude: String = java.lang.String.valueOf(lng)
                val gmmIntentUri: Uri = Uri.parse("google.navigation:q=$latitude,$longitude")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                try {
                    if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(mapIntent)
                    }
                } catch (e: NullPointerException) {
                    Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.message)
                    Toast.makeText(activity, "Couldn't open map", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onClickStartChecking() {
        homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.START_CHECK)
    }

    private fun onClickDeclineOrder() {
        findNavController().navigate(
            MapViewFragmentDirections.actionGlobalCancelReasonsFragment(
                orderId = orderId
            )
        )
    }

}