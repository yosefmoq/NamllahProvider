package com.app.namllahprovider.domain.repository

import com.app.namllahprovider.data.api.user.change_available.ChangeAvailableResponse
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileRequest
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileResponse
import com.app.namllahprovider.data.model.UserDto
import io.reactivex.Maybe

interface UserRepository {

    fun changeAvailable(status: Boolean): Maybe<ChangeAvailableResponse>

    fun getUserProfile(): Maybe<UserDto>

    fun updateUserProfile(updateUserProfileRequest: UpdateUserProfileRequest): Maybe<UpdateUserProfileResponse>

}