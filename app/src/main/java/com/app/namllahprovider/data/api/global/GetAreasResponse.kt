package com.app.namllahprovider.data.api.global

import com.app.namllahprovider.data.model.AreaDto
import com.google.gson.annotations.SerializedName


data class GetAreasResponse (
    @SerializedName("data") var areasList : List<AreaDto>,
    @SerializedName("status") var status : Boolean,
    @SerializedName("msg") var msg : String
)