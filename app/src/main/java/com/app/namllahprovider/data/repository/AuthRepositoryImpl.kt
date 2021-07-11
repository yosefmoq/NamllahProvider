package com.app.namllahprovider.data.repository

import com.app.namllahprovider.data.api.auth.AuthApiImpl
import com.app.namllahprovider.data.api.auth.check_reset_password.CheckResetPasswordResponse
import com.app.namllahprovider.data.api.auth.forget_password.ForgetPasswordResponse
import com.app.namllahprovider.data.api.auth.reset_password.ResetPasswordResponse
import com.app.namllahprovider.data.api.auth.sign_in.SignInResponse
import com.app.namllahprovider.data.api.auth.sign_up.SignUpResponse
import com.app.namllahprovider.data.api.auth.verification_code.VerificationCodeResponse
import com.app.namllahprovider.domain.repository.AuthRepository
import io.reactivex.Maybe
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiImpl: AuthApiImpl
) : AuthRepository {

    override fun signIn(phoneNumber: String, password: String): Maybe<SignInResponse> =
        authApiImpl.signIn(phoneNumber, password)

    override fun signUp(
        userName: String,
        phoneNumber: String,
        password: String,
        language: String
    ): Maybe<SignUpResponse> = authApiImpl.signUp(userName, phoneNumber, password, language)

    override fun verifyOTPCode(phoneNumber: String, code: String): Maybe<VerificationCodeResponse> =
        authApiImpl.verifyOTPCode(phoneNumber, code)

    override fun checkResetPassword(phoneNumber: String, code: String): Maybe<CheckResetPasswordResponse> =
        authApiImpl.checkResetPassword(phoneNumber, code)

    override fun resendOTPCode(phoneNumber: String): Maybe<ForgetPasswordResponse> =
        authApiImpl.resendOTPCode(phoneNumber)

    override fun forgetPassword(phoneNumber: String): Maybe<ForgetPasswordResponse> =
        authApiImpl.forgetPassword(phoneNumber)

    override fun resetPassword(
        phoneNumber: String,
        password: String,
        code: String
    ): Maybe<ResetPasswordResponse> =
        authApiImpl.resetPassword(phoneNumber, password, code)
}