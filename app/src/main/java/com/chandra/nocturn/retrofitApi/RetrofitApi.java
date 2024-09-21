package com.chandra.nocturn.retrofitApi;

import com.chandra.nocturn.modals.DataModalForAuth;
import com.chandra.nocturn.modals.DataModalForVerifyOtp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitApi {
    @POST("send-otp")
    Call<DataModalForAuth> createUserAuth(@Body DataModalForAuth dataModalForAuth);

    @POST("verify-otp")
    Call<DataModalForVerifyOtp> verifyUserOtp(@Body DataModalForVerifyOtp dataModalForVerifyOtp);

}
