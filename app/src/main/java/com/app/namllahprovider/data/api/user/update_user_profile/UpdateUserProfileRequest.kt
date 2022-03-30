package com.app.namllahprovider.data.api.user.update_user_profile

import okhttp3.MultipartBody

data class UpdateUserProfileRequest(
    val name: String = "",
    val mobile: String = "",
    val language: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val services_id: IntArray = intArrayOf(),
    val image: MultipartBody.Part? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UpdateUserProfileRequest

        if (name != other.name) return false
        if (mobile != other.mobile) return false
        if (language != other.language) return false
        if (oldPassword != other.oldPassword) return false
        if (newPassword != other.newPassword) return false
        if (!services_id.contentEquals(other.services_id)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + mobile.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + oldPassword.hashCode()
        result = 31 * result + newPassword.hashCode()
        result = 31 * result + services_id.contentHashCode()
        return result
    }
}