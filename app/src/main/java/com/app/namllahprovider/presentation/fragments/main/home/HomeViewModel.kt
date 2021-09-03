package com.app.namllahprovider.presentation.fragments.main.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.app.namllahprovider.data.api.notification.update_fcm.UpdateFCMTokenRequest
import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderRequest
import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderResponse
import com.app.namllahprovider.data.api.order.list_order.ListOrderRequest
import com.app.namllahprovider.data.api.order.show_order.ShowOrderRequest
import com.app.namllahprovider.data.api.user.change_available.ChangeAvailableResponse
import com.app.namllahprovider.data.model.CancelReasonDto
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.domain.repository.*
import com.app.namllahprovider.domain.utils.OrderStatusRequestType
import com.app.namllahprovider.domain.utils.OrderType
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val configRepository: ConfigRepository,
    private val orderRepository: OrderRepository,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val globalRepository: GlobalRepository,
) : BaseViewModel(application) {

    val getListOrderLiveData = MutableLiveData<List<OrderDto>?>()
    val getLoggedUserLiveData = MutableLiveData<UserDto?>()
    val changeAvailableLiveData = MutableLiveData<ChangeAvailableResponse?>()
    val getOrderDataLiveData = MutableLiveData<OrderDto?>()
    val changeOrderStatusLiveData = MutableLiveData<ChangeOrderResponse?>()
    val cancelReasonsLiveData = MutableLiveData<List<CancelReasonDto>?>()

    fun getListOrderRequest(orderType: OrderType) {
        launch {
            changeLoadingStatus(true)
            disposable.add(
                orderRepository.getListOrder(ListOrderRequest(orderType))
                    .subscribeOn(ioScheduler)
                    .observeOn(ioScheduler)
                    .subscribe({
                        Timber.tag(TAG).d("getListOrderRequest : orderType $orderType, orders $it")
                        changeLoadingStatus(false)
                        getListOrderLiveData.postValue(it)
                    }, {
                        getListOrderLiveData.postValue(null)
                        changeLoadingStatus(false)
                        changeErrorMessage(it)
                    }, {
                        getListOrderLiveData.postValue(null)
                        notifyNoDataComing()
                        changeLoadingStatus(false)
                    })
            )
        }
    }

    fun getLoggedUser() = launch {
        Timber.tag(TAG).d("getLoggedUser : ttt")
        disposable.add(
            userRepository
                .getUserProfile()
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    Timber.tag(TAG).d("getLoggedUser : ttt $it")
                    getLoggedUserLiveData.postValue(it)
                    configRepository.setLoggedUser(it)
                    configRepository.setLanguage(it.language.code)
                    changeLoadingStatus(false)
                }, {
                    getLoggedUserLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    getLoggedUserLiveData.postValue(null)
                    notifyNoDataComing()
                    changeLoadingStatus(false)
                })
        )
    }

    fun changeUserAvailable(newStatus: Boolean) = launch {
        Timber.tag(TAG).d("changeUserAvailable : ttt")
        changeLoadingStatus(true)
        disposable.add(
            userRepository
                .changeAvailable(status = newStatus)
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    Timber.tag(TAG).d("changeUserAvailable : ttt ${it}")
                    getLoggedUser()
                    changeAvailableLiveData.postValue(it)
                }, {
                    changeAvailableLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    changeAvailableLiveData.postValue(null)
                    notifyNoDataComing()
                    changeLoadingStatus(false)
                })
        )
    }

    fun getOrderData(orderId: Int) = launch {
        changeLoadingStatus(true, "Getting order...")
        disposable.add(
            orderRepository
                .showOrder(ShowOrderRequest(orderId))
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    getOrderDataLiveData.postValue(it)
                    changeLoadingStatus(false)
                }, {
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    notifyNoDataComing()
                    changeLoadingStatus(false)
                })
        )
    }

    fun changeOrderStatus(orderId: Int, orderStatusRequestType: OrderStatusRequestType) = launch {
        changeLoadingStatus(true, orderStatusRequestType.label ?: "Loading")
        disposable.add(
            orderRepository.changeOrderStatus(
                ChangeOrderRequest(
                    orderId = orderId,
                    orderStatusRequestType = orderStatusRequestType
                )
            ).subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    changeOrderStatusLiveData.postValue(it)
                    changeLoadingStatus(false)
                }, {
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    notifyNoDataComing()
                    changeLoadingStatus(false)
                })
        )
    }

    fun changeOrderStatus(
        orderId: Int,
        orderStatusRequestType: OrderStatusRequestType,
        estimatedTime: Int,
        estimatedPriceParts: Double,
        checkDescription: String = "",
    ) = launch {
        changeLoadingStatus(true, orderStatusRequestType.label ?: "Loading")
        disposable.add(
            orderRepository.changeOrderStatus(
                ChangeOrderRequest(
                    orderId = orderId,
                    orderStatusRequestType = orderStatusRequestType,
                    estimatedTime = estimatedTime,
                    estimatedPriceParts = estimatedPriceParts,
                    checkDescription = checkDescription
                )
            ).subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    changeOrderStatusLiveData.postValue(it)
                    changeLoadingStatus(false)
                }, {
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    notifyNoDataComing()
                    changeLoadingStatus(false)
                })
        )
    }

    fun changeOrderStatus(
        orderId: Int,
        orderStatusRequestType: OrderStatusRequestType,
        boughtPrice: RequestBody,
        bringTimes: RequestBody,
        bills: List<MultipartBody.Part>?,
    ) = launch {
        changeLoadingStatus(true, orderStatusRequestType.label ?: "Loading")
        disposable.add(
            orderRepository.changeOrderStatus(
                ChangeOrderRequest(
                    orderId = orderId,
                    orderStatusRequestType = orderStatusRequestType,
                    boughtPrice = boughtPrice,
                    bringTimes = bringTimes,
                    bills = bills
                )
            ).subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    changeOrderStatusLiveData.postValue(it)
                    changeLoadingStatus(false)
                }, {
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    notifyNoDataComing()
                    changeLoadingStatus(false)
                })
        )
    }

    fun changeOrderStatus(
        orderId: Int,
        orderStatusRequestType: OrderStatusRequestType,
        amount: Double,
    ) = launch {
        changeLoadingStatus(true, orderStatusRequestType.label ?: "Loading")
        disposable.add(
            orderRepository.changeOrderStatus(
                ChangeOrderRequest(
                    orderId = orderId,
                    orderStatusRequestType = orderStatusRequestType,
                    amount = amount.toInt()
                )
            ).subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    changeOrderStatusLiveData.postValue(it)
                    changeLoadingStatus(false)
                }, {
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    notifyNoDataComing()
                    changeLoadingStatus(false)
                })
        )
    }

    fun changeOrderStatus(
        orderId: Int,
        orderStatusRequestType: OrderStatusRequestType,
        cancelReasonId: Int
    ) = launch {
        changeLoadingStatus(true, orderStatusRequestType.label ?: "Loading")
        disposable.add(
            orderRepository.changeOrderStatus(
                ChangeOrderRequest(
                    orderId = orderId,
                    orderStatusRequestType = orderStatusRequestType,
                    cancelReasonId = cancelReasonId
                )
            ).subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    changeOrderStatusLiveData.postValue(it)
                    changeLoadingStatus(false)
                }, {
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    notifyNoDataComing()
                    changeLoadingStatus(false)
                })
        )
    }

    fun updateUserFcmToken(fcmToken: String) {
        launch {
            configRepository.setFCMToken(fcmToken)
            disposable.add(
                notificationRepository.updateFCMToken(
                    UpdateFCMTokenRequest(fcmToken = fcmToken)
                ).subscribeOn(ioScheduler)
                    .observeOn(ioScheduler)
                    .subscribe()
            )
        }
    }

    fun getCancelReasons() = launch {
        changeLoadingStatus(true, "Loading")
        disposable.add(
            globalRepository
                .getCancelReasons()
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    cancelReasonsLiveData.postValue(it)
                    changeLoadingStatus(false)
                }, {
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    notifyNoDataComing()
                    changeLoadingStatus(false)
                })
        )
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}