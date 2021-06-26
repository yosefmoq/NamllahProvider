package com.app.namllahprovider.data.api.user

import com.app.namllahprovider.data.api.BaseResponse
import com.app.namllahprovider.data.api.user.change_available.ChangeAvailableRequest
import com.app.namllahprovider.data.api.user.change_available.ChangeAvailableResponse
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileResponse
import com.app.namllahprovider.data.api.user.user_profile.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    @POST("provider/change-available")
    fun changeAvailable(
//        @Header("Authorization") token: String,
        @Body changeAvailableRequest: ChangeAvailableRequest
    ): Call<ChangeAvailableResponse>

    @GET("user/profile")
    fun getUser(): Call<UserProfileResponse>


    @FormUrlEncoded
    @POST("user/profile")
    fun updateUserName(@Field("name") name: String): Call<UpdateUserProfileResponse>

    @FormUrlEncoded
    @POST("user/profile")
    fun updateUserMobileNumber(@Field("mobile") mobile: String): Call<UpdateUserProfileResponse>

    @FormUrlEncoded
    @POST("user/profile")
    fun updateUserLanguage(@Field("language") language: String): Call<UpdateUserProfileResponse>

    @FormUrlEncoded
    @POST("user/profile")
    fun updateUserPassword(
        @Field("old_password") oldPassword: String,
        @Field("password") newPassword: String,
    ): Call<UpdateUserProfileResponse>

    @FormUrlEncoded
    @POST("user/profile")
    fun updateUserService(@Field("services_id[]") services_id: Array<Int>): Call<UpdateUserProfileResponse>

    @FormUrlEncoded
    @POST("user/change-setting")
    fun updateUserSettings(
        @Field("key") key: String,
        @Field("value") value: Boolean
    ): Call<BaseResponse>

    @Multipart
    @POST("user/profile")
    fun updateUserPhoto(@Part image: MultipartBody.Part): Call<UpdateUserProfileResponse>

    @GET("user/logout")
    fun logout(): Call<BaseResponse>
}