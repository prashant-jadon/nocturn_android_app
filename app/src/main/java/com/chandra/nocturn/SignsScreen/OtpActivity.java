package com.chandra.nocturn.SignsScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chandra.nocturn.ProfileCreation.ProfilecreationActivity;
import com.chandra.nocturn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class OtpActivity extends AppCompatActivity {

    TextView otpInput;
    Button verifyOtp;
    SharedPreferences sharedPreferences;
    String url = "http://192.168.0.105:8000/api/v1/auth/verify-otp";

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


        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");

        verifyOtp.setOnClickListener(view -> {
            String otp = otpInput.getText().toString();
            if (otp.isEmpty()) {
                Toast.makeText(OtpActivity.this, "Enter OTP to verify", Toast.LENGTH_SHORT).show();
                return;
            }

            verifyOTPandJWT(getApplicationContext(),otp,phoneNumber);
        });
    }

    private void verifyOTPandJWT(Context context,String otp,String phoneNumber){
        RequestQueue requestQueue =  Volley.newRequestQueue(this);
        JsonObjectRequest otpVerificationRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String jwtToken = response.getString("accessToken");
                    sharedPreferences = getSharedPreferences("NocturnSharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("jwtAuth",jwtToken);
                    editor.commit();
                    Toast.makeText(context, "Create your profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OtpActivity.this, ProfilecreationActivity.class));
                    finish();
                }catch (JSONException e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public byte[] getBody() {
                try {
                    JSONObject otpVerificationBody = new JSONObject();
                    otpVerificationBody.put("otp",otp);
                    otpVerificationBody.put("phoneNumber",phoneNumber);
                    return otpVerificationBody.toString().getBytes(StandardCharsets.UTF_8);
                }catch (Exception e){
                    Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        };
        requestQueue.add(otpVerificationRequest);
    }



}