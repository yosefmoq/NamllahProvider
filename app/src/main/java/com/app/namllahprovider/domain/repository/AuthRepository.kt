package com.app.namllahprovider.domain.repository

import com.app.namllahprovider.data.api.auth.check_reset_password.CheckResetPasswordResponse
import com.app.namllahprovider.data.api.auth.forget_password.ForgetPasswordResponse
import com.app.namllahprovider.data.api.auth.reset_password.ResetPasswordResponse
import com.app.namllahprovider.data.api.auth.sign_in.SignInResponse
import com.app.namllahprovider.data.api.auth.sign_up.SignUpResponse
import com.app.namllahprovider.data.api.auth.verification_code.VerificationCodeResponse
import io.reactivex.Maybe

interface AuthRepository {

    fun signIn(phoneNumber: String, password: String): Maybe<SignInResponse>

    fun signUp(
        userName: String,
        phoneNumber: String,
        password: String,
        language: String
    ): Maybe<SignUpResponse>

    fun verifyOTPCode(phoneNumber: String, code: String): Maybe<VerificationCodeResponse>

    fun checkResetPassword(phoneNumber: String, code: String): Maybe<CheckResetPasswordResponse>

    fun resendOTPCode(phoneNumber: String): Maybe<ForgetPasswordResponse>

    fun forgetPassword(phoneNumber: String): Maybe<ForgetPasswordResponse>

    fun resetPassword(
        phoneNumber: String,
        password: String,
        code: String
    ): Maybe<ResetPasswordResponse>
}