package com.app.namllahprovider.data.api.user

import com.app.namllahprovider.data.api.user.change_available.ChangeAvailableRequest
import com.app.namllahprovider.data.api.user.change_available.ChangeAvailableResponse
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileRequest
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileResponse
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.domain.repository.ConfigRepository
import io.reactivex.Maybe
import javax.inject.Inject

class UserApiImpl @Inject constructor(
    private val userApi: UserApi,
    private val configRepository: ConfigRepository
) {

    fun changeAvailable(status: Boolean): Maybe<ChangeAvailableResponse> =
        Maybe.create {
            val response = userApi.changeAvailable(
                changeAvailableRequest = ChangeAvailableRequest(if (status) 1 else 0)
            ).execute()
            if (response.isSuccessful) {
                val changeAvailableResponse = response.body()
                if (changeAvailableResponse == null) {
                    it.onError(Throwable("Something Error, Please try again later"))
                } else {
                    it.onSuccess(changeAvailableResponse)
                }
            } else {
                it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
            }
        }

    fun getUserProfile(): Maybe<UserDto> =
        Maybe.create {
            val response = userApi.getUser().execute()
            if (response.isSuccessful) {
                val userProfileResponse = response.body()
                if (userProfileResponse == null) {
                    it.onError(Throwable("Something Error, Please try again later"))
                } else {
                    if (userProfileResponse.data == null) {
                        it.onError(Throwable("Something Error, Please try again later"))
                    } else {
                        it.onSuccess(userProfileResponse.data)
                    }
                }
            } else {
                it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
            }
        }

    fun updateUserProfile(updateUserProfileRequest: UpdateUserProfileRequest): Maybe<UpdateUserProfileResponse> =
        Maybe.create {
            val response = userApi.updateUserProfile(
                updateUserProfileRequest = updateUserProfileRequest,
            ).execute()
            if (response.isSuccessful) {
                val updateUserProfileResponse = response.body()
                if (updateUserProfileResponse == null) {
                    it.onError(Throwable("Something Error, Please try again later"))
                } else {
                    it.onSuccess(updateUserProfileResponse)
                }
            } else {
                it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
            }
        }

    companion object {
        private const val TAG = "UserApiImpl"
    }
}