package com.app.namllahprovider.data.api.user

import com.app.namllahprovider.data.api.user.change_available.ChangeAvailableRequest
import com.app.namllahprovider.data.api.user.change_available.ChangeAvailableResponse
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileRequest
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileResponse
import com.app.namllahprovider.data.api.user.user_profile.UserProfileResponse
import com.app.namllahprovider.data.model.UserDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @POST("provider/change-available")
    fun changeAvailable(
//        @Header("Authorization") token: String,
        @Body changeAvailableRequest: ChangeAvailableRequest
    ): Call<ChangeAvailableResponse>

    @GET("user/profile")
    fun getUser(): Call<UserProfileResponse>

    @POST("user/profile")
    fun updateUserProfile(
//        @Header("Authorization") token: String,
        @Body updateUserProfileRequest: UpdateUserProfileRequest,
    ): Call<UpdateUserProfileResponse>
}