package com.app.namllahprovider.data.repository

import com.app.namllahprovider.data.api.user.UserApiImpl
import com.app.namllahprovider.data.api.user.change_available.ChangeAvailableResponse
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileRequest
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileResponse
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.domain.repository.UserRepository
import io.reactivex.Maybe
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiImpl: UserApiImpl
) : UserRepository {

    override fun changeAvailable(status: Boolean): Maybe<ChangeAvailableResponse> =
        userApiImpl.changeAvailable(status)

    override fun getUserProfile(): Maybe<UserDto> =
        userApiImpl.getUserProfile()


    override fun updateUserProfile(updateUserProfileRequest: UpdateUserProfileRequest): Maybe<UpdateUserProfileResponse> =
        userApiImpl.updateUserProfile(updateUserProfileRequest)
}