package com.chandra.nocturn.SignsScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chandra.nocturn.MainActivity;
import com.chandra.nocturn.R;
import com.chandra.nocturn.modals.DataModalForAuth;
import com.chandra.nocturn.modals.DataModalForVerifyOtp;
import com.chandra.nocturn.retrofitApi.RetrofitApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtpActivity extends AppCompatActivity {

    TextView otpInput;
    Button verifyOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp);

        // Applying WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        otpInput = findViewById(R.id.otpInput);
        verifyOtp = findViewById(R.id.verifyOtpButton);

        // Retrieve Bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey("phoneNumber")) {
            Toast.makeText(this, "Phone number not provided", Toast.LENGTH_SHORT).show();
            Log.e("OtpActivity", "No phone number found in the intent bundle");
            finish(); // Exit the activity as no phone number was provided
            return;
        }

        String phoneNumber = bundle.getString("phoneNumber");

        verifyOtp.setOnClickListener(view -> {
            String otp = otpInput.getText().toString();
            if (otp.isEmpty()) {
                Toast.makeText(OtpActivity.this, "Enter OTP to verify", Toast.LENGTH_SHORT).show();
                return;
            }

            verifyOtp(otp, phoneNumber);
        });
    }


    private void verifyOtp(String otp,String phoneNumber){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:8000/api/v1/auth/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        DataModalForVerifyOtp dataModalForVerifyOtp = new DataModalForVerifyOtp(otp,phoneNumber);
        Call<DataModalForVerifyOtp> call = retrofitApi.verifyUserOtp(dataModalForVerifyOtp);
        call.enqueue(new Callback<DataModalForVerifyOtp>() {
            @Override
            public void onResponse(Call<DataModalForVerifyOtp> call, Response<DataModalForVerifyOtp> response) {
                if(response.isSuccessful()){
                    Toast.makeText(OtpActivity.this, "OTP VERIFIED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    DataModalForVerifyOtp responseFromAPI = response.body();
                    if(responseFromAPI != null){
                        Toast.makeText(OtpActivity.this, responseFromAPI.toString(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OtpActivity.this, MainActivity.class));
                        finish();
                    }

                }
            }

            @Override
            public void onFailure(Call<DataModalForVerifyOtp> call, Throwable throwable) {
                Toast.makeText(OtpActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("error", throwable.getMessage());
            }
        });
    }
}