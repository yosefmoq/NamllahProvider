package com.app.namllahprovider.presentation.fragments.main.home.work

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.data.model.UserFirebaseJaveModel
import com.app.namllahprovider.databinding.FragmentWorkBinding
import com.app.namllahprovider.domain.utils.OrderStatusRequestType
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.fragments.main.home.order_details.OrderDetailsFragmentArgs
import com.app.namllahprovider.presentation.utils.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.MetadataChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class WorkFragment : Fragment(), View.OnClickListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentWorkBinding: FragmentWorkBinding? = null
    private var orderId: Int = -1
    private var orderDto: OrderDto? = null
    private var isCompleteToBack = false
    lateinit var countDownTimer: CountDownTimer
    private var isWorking = false
    private lateinit var myDocumentReference: DocumentReference
    private lateinit var counter: Disposable


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
        fragmentWorkBinding = FragmentWorkBinding.inflate(inflater, container, false)
        fragmentWorkBinding!!.root.isFocusableInTouchMode = true
        fragmentWorkBinding!!.root.requestFocus()

        myDocumentReference = getUserDocument(homeViewModel.getId().toString())
        fragmentWorkBinding!!.root.setOnKeyListener(object : View.OnKeyListener {

            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (orderDto!!.isWorking == 1) {
                            homeViewModel.changeOrderStatus(
                                orderId = orderId,
                                OrderStatusRequestType.STOP_WORK
                            )
                            isCompleteToBack = true
                        } else {
                            findNavController().popBackStack()

                        }
                        return true
                    }
                }
                return false
            }
        })
        return fragmentWorkBinding?.apply {
            actionOnClick = this@WorkFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        getOrderData()
        observeLiveData()
        fragmentWorkBinding!!.tvWorkTimer.keepScreenOn = true

    }

    private fun initToolbar() {
        val toolbar = fragmentWorkBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.work)

        toolbar?.root?.setNavigationOnClickListener {
            if (orderDto!!.isWorking == 1) {
                homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.STOP_WORK)
                isCompleteToBack = true
            } else {
                findNavController().popBackStack()

            }
        }
    }

    private fun getOrderData() {
        if (orderId != -1) {
            homeViewModel.getOrderData(orderId)
            homeViewModel.getOrderDataLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    orderDto = it
                    fragmentWorkBinding?.apply {
                        orderDto = this@WorkFragment.orderDto
                        when (orderDto?.status?.getOrderStatus()) {
                            OrderStat.CHECK -> {
                                btnStartWorkTimer.visibility = View.VISIBLE
                                llBtnsContainer.visibility = View.GONE
                            }
                            OrderStat.WORKING -> {
                                btnStartWorkTimer.visibility = View.GONE
                                llBtnsContainer.visibility = View.VISIBLE
                                if (orderDto?.isWorking == 1) {

                                    resumeWork()

                                } else {
                                    pauseWork()
                                }
                            }
                            else -> {
                                SweetAlert.instance.showFailAlert(
                                    activity = requireActivity(),
                                    message = "Couldn't Update Order Status"
                                )
                                findNavController().popBackStack(R.id.mainFragment, false)
                            }
                        }
                    }


                }
            }
        } else {
            Timber.tag(TAG).d("onViewCreated : Can't find order details")
        }

    }

    private fun resumeWork(old: Long = 0) {
        val oldDuration = orderDto?.duration!!
        isWorking = true
        updateUIs()
    }

    private fun pauseWork() {
        isWorking = false
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        updateUIs()
    }

    private fun updateUIs() {
        Timber.tag(TAG).d("updateUIs : orderDto $orderDto")
        fragmentWorkBinding?.apply {
//            tvWorkTimer.text = (this@WorkFragment.orderDto?.duration ?: 0).getTime()

            btnStartWorkTimer.visibility = View.GONE
            llBtnsContainer.visibility = View.VISIBLE
            Timber.tag(TAG).d("updateUIs : orderDto $orderDto")

            if (orderDto?.isWorking == 1) {
                btnPauseResumeTime.text = getString(R.string.pause)
            } else {
                btnPauseResumeTime.text = getString(R.string.resume)
            }
        }
    }

    private fun initCounter(initialTime: Int) {
//        val est = homeViewModel.getCheckTime()
        val totalMillis: Long = if (orderDto!!.estimatedTime != 0.0) {
            (orderDto!!.estimatedTime!! * 60 * 60 * 1000).toLong()

        } else {
            360000000L
        }

        if (orderDto!!.duration!! >= orderDto!!.estimatedTime!! * 60 * 60 * 1000) {
//            onClickFinishWork()

        } else {


            countDownTimer = object : CountDownTimer(totalMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val goingMillis = totalMillis.minus(millisUntilFinished).plus(initialTime)
                    val goingTime = goingMillis.getTime()

                    val remainingTime = millisUntilFinished.getTime()
                    Timber.tag(TAG).d("onTick : Going Time $goingTime")
                    Timber.tag(TAG).d("onTick : Remaining Time $remainingTime")
                    fragmentWorkBinding?.tvWorkTimer?.text = goingTime
                }

                override fun onFinish() {
                    onClickFinishWork()
                }
            }
            countDownTimer.start()
        }
        Log.v("ttt", "Total TIME :: $totalMillis")


    }

    fun countUp(userFirebaseJaveModel: UserFirebaseJaveModel) {


        counter = io.reactivex.Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                val differance = userFirebaseJaveModel.complete_at.getDifferance()
                val timeAsLong = userFirebaseJaveModel.duration + (differance * 1000)

                if (timeAsLong >= orderDto!!.estimatedTime!! * 60 * 60 * 1000) {
                    counter.dispose()

                    requireActivity().runOnUiThread {
                        onClickFinishWork()

                    }


                } else {
                    fragmentWorkBinding?.tvWorkTimer?.post(Runnable {
                        fragmentWorkBinding?.tvWorkTimer?.text = timeAsLong.getTime()
                    })
                }

            }


        /*   var totalMillis: Long = if (orderDto!!.estimatedTime != 0.0) {
               (orderDto!!.estimatedTime!! * 60 * 60 * 1000).toLong()
           } else {
               360000000L
           }
   */
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
//                SweetAlert.instance.showFailAlert(activity = requireActivity(), throwable = it)
                it.printStackTrace()
            }
        }

        homeViewModel.changeOrderStatusLiveData.observe(viewLifecycleOwner) {
            it?.let { it ->
                Timber.tag(TAG).d("observeLiveData ChangeOrderStatus : $it")
                Timber.tag(TAG)
                    .d("observeLiveData : ChangeOrderStatus OrderId:${it.order?.id} to ${it.order?.status?.getOrderStatus()}")
                if (it.status) {
                    orderDto = it.order
                    fragmentWorkBinding?.apply {
                        orderDto = this@WorkFragment.orderDto
                    }
                    when (it.order?.status?.getOrderStatus()) {
                        OrderStat.WORKING -> {
                            if (isCompleteToBack) {
                                findNavController().popBackStack()
                            }

                            //Update Timer Start/Stop
                            if (orderDto?.isWorking == 1) {
                                resumeWork()

                            } else {
                                pauseWork()
                            }
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

    companion object {
        private const val TAG = "WorkFragment"
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentWorkBinding?.btnStartWorkTimer -> onClickStartWork()
            fragmentWorkBinding?.btnPauseResumeTime -> onClickPauseResumeWork()
            fragmentWorkBinding?.btnFinishWork -> onClickFinishWork()
        }
    }

    private fun onClickFinishWork() {
        homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.STOP_WORK)

        findNavController().navigate(
            WorkFragmentDirections.actionWorkFragmentToBillFragment(
                orderId = orderId
            )
        )

        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    private fun onClickPauseResumeWork() {
        if (orderDto?.isWorking == 1) {
            homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.STOP_WORK)
        } else {
            homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.START_WORK)
        }
    }

    private fun onClickStartWork() {
        homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.START_WORK)
    }

    override fun onResume() {
        super.onResume()
        myDocumentReference.addSnapshotListener(MetadataChanges.EXCLUDE) { value, _ ->
            if (value != null) {
                if (this::counter.isInitialized)
                    counter.dispose()
                isWorking = false
                handleChangeDataInMyDocument(value)

            }
        }
    }

    private fun handleChangeDataInMyDocument(value: DocumentSnapshot) {
        val userFirebaseJavaModel = value.toObject(UserFirebaseJaveModel::class.java)
        if (userFirebaseJavaModel != null) {
            isWorking = userFirebaseJavaModel.isIs_working
            if (isWorking) {
                countUp(userFirebaseJavaModel)
            } else {
                if (userFirebaseJavaModel.status_id == 6) {
                    fragmentWorkBinding?.tvWorkTimer?.visibility = View.VISIBLE
                    fragmentWorkBinding?.tvWorkTimer?.text =
                        userFirebaseJavaModel.duration.getTime()
                }
            }
        }

    }

}

