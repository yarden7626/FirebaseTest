package com.example.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        TextView tv = findViewById(R.id.helloText);

        // Check if a user is logged in
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in, display the email
            tv.setText("Hello, " + user.getEmail());
        } else {
            // No user is signed in, redirect to Login activity
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
            return; // Exit the method to prevent further execution
        }

        Button btnLogout = findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                mAuth.signOut();

                // Redirect to LoginActivity after logout
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
