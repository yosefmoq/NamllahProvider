package com.app.namllahprovider.domain.repository

import com.app.namllahprovider.data.model.AreaDto
import com.app.namllahprovider.data.model.ServiceDto
import io.reactivex.Maybe

interface GlobalRepository {
    fun getServices(): Maybe<List<ServiceDto>>

    fun getAreas(): Maybe<List<AreaDto>>
}