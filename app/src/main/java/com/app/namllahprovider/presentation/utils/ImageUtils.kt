package com.app.namllahprovider.presentation.utils

import com.app.namllahprovider.data.model.ImageDto


fun ImageDto?.getImageUrl(): String {
    return if (this?.med.isNullOrEmpty()) this?.original?:"" else this?.med?:""
}