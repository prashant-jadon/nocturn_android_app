package com.chandra.nocturn.SignsScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chandra.nocturn.R;
import com.chandra.nocturn.modals.DataModalForAuth;
import com.chandra.nocturn.retrofitApi.RetrofitApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignscreenActivity extends AppCompatActivity {
    EditText numberInput;
    Button sendOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signscreen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        numberInput = findViewById(R.id.phoneNumberInput);
        sendOtp = findViewById(R.id.sendOtpButton);



        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = numberInput.getText().toString();
                if(phoneNumber.isEmpty()){
                    Toast.makeText(SignscreenActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                createUserAuth(phoneNumber);
            }
        });
    }

    private void createUserAuth(String phoneNumber) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.105:8000/api/v1/auth/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        DataModalForAuth dataModalForAuth = new DataModalForAuth(phoneNumber);
        Call<DataModalForAuth> call = retrofitApi.createUserAuth(dataModalForAuth);
        call.enqueue(new Callback<DataModalForAuth>() {
            @Override
            public void onResponse(Call<DataModalForAuth> call, Response<DataModalForAuth> response) {
                Toast.makeText(SignscreenActivity.this, "Please check for the OTP", Toast.LENGTH_SHORT).show();

                DataModalForAuth responseFromAPI = response.body();
                if(responseFromAPI != null) {
                    // Only start the OTP Activity here, after a successful response
                    Intent intent = new Intent(SignscreenActivity.this, OtpActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber); // Pass phone number to OTP Activity
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignscreenActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataModalForAuth> call, Throwable throwable) {
                Toast.makeText(SignscreenActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}