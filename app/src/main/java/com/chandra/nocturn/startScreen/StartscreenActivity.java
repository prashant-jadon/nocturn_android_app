package com.chandra.nocturn.startScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chandra.nocturn.MainActivity;
import com.chandra.nocturn.ProfileCreation.ProfilecreationActivity;
import com.chandra.nocturn.R;
import com.chandra.nocturn.SignsScreen.OtpActivity;
import com.chandra.nocturn.SignsScreen.SignscreenActivity;

public class StartscreenActivity extends AppCompatActivity {

    Button loginButton,signUpButton;
    SharedPreferences awt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_startscreen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signupButton);
        awt = getSharedPreferences("NocturnSharedPref", Context.MODE_PRIVATE);
        String token = awt.getString("jwtAuth", "");
        String email = awt.getString("email","");


        if(!token.isEmpty() && !email.isEmpty()){
            startActivity(new Intent(StartscreenActivity.this, MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(StartscreenActivity.this, ProfilecreationActivity.class));
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartscreenActivity.this, SignscreenActivity.class));
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartscreenActivity.this, SignscreenActivity.class));
                finish();
            }
        });
    }
}