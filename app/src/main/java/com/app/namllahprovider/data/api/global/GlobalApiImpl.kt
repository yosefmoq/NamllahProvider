package com.app.namllahprovider.data.api.global

import com.app.namllahprovider.data.model.AreaDto
import com.app.namllahprovider.data.model.CancelReasonDto
import com.app.namllahprovider.data.model.ServiceDto
import com.app.namllahprovider.domain.repository.ConfigRepository
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

class GlobalApiImpl @Inject constructor(
    private val globalApi: GlobalApi,
    private val configRepository: ConfigRepository
) {
    fun getServices(): Maybe<List<ServiceDto>> = Maybe.create {
        val response = globalApi.getServices().execute()
        if (response.isSuccessful) {
            val servicesResponse = response.body()
            Timber.tag(TAG).d("getServices : servicesResponse $servicesResponse")
            if (servicesResponse == null) {
                it.onError(Throwable("Something Error, Please try again later"))
            } else {
                if (servicesResponse.servicesList.isEmpty()) {
                    it.onComplete()
                } else {
                    it.onSuccess(servicesResponse.servicesList)
                }
            }
        } else {
            //Call on Error
            it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
        }
    }

    fun getAreas(): Maybe<List<AreaDto>> = Maybe.create {
        val response = globalApi.getAreas().execute()
        if (response.isSuccessful) {
            val areasResponse = response.body()
            Timber.tag(TAG).d("getAreas : areasResponse $areasResponse")
            if (areasResponse == null) {
                it.onError(Throwable("Something Error, Please try again later"))
            } else {
                if (areasResponse.areasList.isEmpty()) {
                    it.onComplete()
                } else {
                    it.onSuccess(areasResponse.areasList)
                }
            }
        } else {
            //Call on Error
            it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
        }
    }

    fun getCancelReasons(): Maybe<List<CancelReasonDto>> = Maybe.create {
        val response = globalApi.getCancelReasons().execute()
        if (response.isSuccessful) {
            val cancelReasonsResponse = response.body()
            Timber.tag(TAG).d("getCancelReasons : cancelReasonsResponse $cancelReasonsResponse")
            if (cancelReasonsResponse == null) {
                it.onError(Throwable("Something Error, Please try again later"))
            } else {
                if (cancelReasonsResponse.cancelReasonsList.isNullOrEmpty()) {
                    it.onComplete()
                } else {
                    it.onSuccess(cancelReasonsResponse.cancelReasonsList)
                }
            }
        } else {
            //Call on Error
            it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
        }
    }

    companion object {
        private const val TAG = "GlobalApiImpl"
    }
}