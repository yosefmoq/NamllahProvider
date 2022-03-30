package com.app.namllahprovider.data.api.auth

import com.app.namllahprovider.data.api.auth.check_reset_password.CheckResetPasswordRequest
import com.app.namllahprovider.data.api.auth.forget_password.ForgetPasswordRequest
import com.app.namllahprovider.data.api.auth.reset_password.ResetPasswordRequest
import com.app.namllahprovider.data.api.auth.sign_in.SignInRequest
import com.app.namllahprovider.data.api.auth.sign_up.SignUpRequest
import com.app.namllahprovider.data.api.auth.verification_code.VerificationCodeRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    fun signIn(
//        @Header("Authorization") token: String,
        @Body signInRequest: SignInRequest
    ): Call<ResponseBody>

    @POST("auth/register-provider")
    fun signUp(
//        @Header("Authorization") token: String,
        @Body signUpRequest: SignUpRequest
    ): Call<ResponseBody>

    @POST("auth/activate-mobile")
    fun verifyOTPCode(
//        @Header("Authorization") token: String,
        @Body verificationCodeRequest: VerificationCodeRequest
    ): Call<ResponseBody>

    @POST("auth/resend-code")
    fun resendOTPCode(
//        @Header("Authorization") token: String,
        @Body forgetPasswordRequest: ForgetPasswordRequest
    ): Call<ResponseBody>

    @POST("auth/forget-password")
    fun forgetPassword(
//        @Header("Authorization") token: String,
        @Body forgetPasswordRequest: ForgetPasswordRequest
    ): Call<ResponseBody>


    @POST("auth/check-reset-password")
    fun checkResetPassword(
//        @Header("Authorization") token: String,
        @Body checkResetPasswordRequest: CheckResetPasswordRequest
    ): Call<ResponseBody>

    @POST("auth/reset-password")
    fun resetPassword(
//        @Header("Authorization") token: String,
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Call<ResponseBody>
}