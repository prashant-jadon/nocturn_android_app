package com.chandra.nocturn.SignsScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chandra.nocturn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;


public class SignscreenActivity extends AppCompatActivity {
    EditText numberInput;
    Button sendOtp;

    String url = "http://192.168.0.105:8000/api/v1/auth/send-otp";
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
                sendOtpRequestFunction(getApplicationContext(),phoneNumber);
            }
        });
    }

    private void sendOtpRequestFunction(Context context,String phoneNumber){


        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest sendOtpRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent intent = new Intent(SignscreenActivity.this, OtpActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber); // Pass phone number to OTP Activity
                    startActivity(intent);
                    finish();

                    String successMsg = response.getString("msg");
                    Toast.makeText(context, "Check your messages for OTP", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignscreenActivity.this,OtpActivity.class));
                    finish();
                }catch (JSONException e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               try {
                   Toast.makeText(context,error.getMessage(), Toast.LENGTH_SHORT).show();
               }catch (Exception e){
                   Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
        }){
            @Override
            public byte[] getBody() {
             try {
                 JSONObject otpRequestBody = new JSONObject();
                 otpRequestBody.put("phoneNumber",phoneNumber);
                 return otpRequestBody.toString().getBytes(StandardCharsets.UTF_8);
             }catch (Exception e){
                 Toast.makeText(SignscreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                 return  null;
             }
            }
        };
        queue.add(sendOtpRequest);
    }


}