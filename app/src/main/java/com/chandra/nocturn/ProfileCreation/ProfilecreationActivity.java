package com.chandra.nocturn.ProfileCreation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.Volley;
import com.chandra.nocturn.MainActivity;
import com.chandra.nocturn.R;
import com.chandra.nocturn.SignsScreen.OtpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ProfilecreationActivity extends AppCompatActivity {

    EditText Euname,eEmail,Efname;
    Button createAccountButton;
    SharedPreferences awt ;
    String url = "http://192.168.0.105:8000/api/v1/user/updateUserInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profilecreation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Euname = findViewById(R.id.username);
        eEmail = findViewById(R.id.email);
        Efname = findViewById(R.id.fullName);

        createAccountButton = findViewById(R.id.createAccount);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = Euname.getText().toString();
                String email = eEmail.getText().toString();
                String fullName = Efname.getText().toString();

                if(username.isEmpty() || email.isEmpty() || fullName.isEmpty()){
                    return;
                }
                creatProfile(getApplicationContext(),username,email,fullName);
            }
        });
    }

    private  void creatProfile(Context context,String username,String email,String fullName){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        awt = getSharedPreferences("NocturnSharedPref", Context.MODE_PRIVATE);
        String token = awt.getString("jwtAuth", "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String emailForUser = response.getString("email");
                    SharedPreferences.Editor editor = awt.edit();
                    editor.putString("email",emailForUser);
                    editor.commit();
                    Toast.makeText(context, "Lets start blind date", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfilecreationActivity.this, MainActivity.class));
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
                    Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization",token);
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject body = new JSONObject();
                    body.put("username", username);
                    body.put("email", email);
                    body.put("fullName", fullName);
                    return body.toString().getBytes(StandardCharsets.UTF_8);
                } catch (Exception e) {
                    Toast.makeText(ProfilecreationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}