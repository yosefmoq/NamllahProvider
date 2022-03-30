package com.app.namllahprovider.data.repository

import com.app.namllahprovider.data.api.global.GlobalApiImpl
import com.app.namllahprovider.data.model.AreaDto
import com.app.namllahprovider.data.model.CancelReasonDto
import com.app.namllahprovider.data.model.ServiceDto
import com.app.namllahprovider.domain.repository.GlobalRepository
import io.reactivex.Maybe
import javax.inject.Inject

class GlobalRepositoryImpl @Inject constructor(
    private val globalApiImpl: GlobalApiImpl
):GlobalRepository {

    override fun getServices(): Maybe<List<ServiceDto>> =
        globalApiImpl.getServices()

    override fun getAreas(): Maybe<List<AreaDto>> =
        globalApiImpl.getAreas()

    override fun getCancelReasons(): Maybe<List<CancelReasonDto>> =
        globalApiImpl.getCancelReasons()
}