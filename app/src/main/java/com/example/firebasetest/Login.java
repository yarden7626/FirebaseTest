package com.example.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity {

    // Declare the EditText fields for user input (email and password)
    private EditText emailEditText, passwordEditText;

    // Declare the buttons for login and sign up
    private Button loginButton, signupButton;
    private CheckBox showPasswordCheckBox;

    // Declare a FirebaseAuth object to handle authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Set the layout for the activity

        // Initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Link the UI elements (EditText and Button) with the corresponding views in the layout
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);

        // Set an onClickListener for the sign-up button to handle user sign-up
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignup();
            }
        });

        // Set an onClickListener for the login button to handle user login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });

        // Add listener to CheckBox to toggle password visibility
        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show password
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Hide password
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // Move cursor to the end of the text
                passwordEditText.setSelection(passwordEditText.length());
            }
        });
    }

    private void handleSignup(){
        // Get the email and password input from the user
        String email = emailEditText.getText().toString().trim(); // Remove leading/trailing spaces
        String password = passwordEditText.getText().toString().trim(); // Remove leading/trailing spaces

        // Check if the email and password are valid (not empty)
        if (validateInputs(email, password)) {
            // Create a new user with the provided email and password using Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Check if the sign-up operation was successful
                            if (task.isSuccessful()) {
                                // Sign-up successful, get the newly created user
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Close the login activity
                                }
                            } else {
                                // Sign-up failed, show the error message
                                Toast.makeText(Login.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void handleLogin(){
        // Get the email and password input from the user
        String email = emailEditText.getText().toString().trim(); // Remove leading/trailing spaces
        String password = passwordEditText.getText().toString().trim(); // Remove leading/trailing spaces

        // Check if the email and password are valid (not empty)
        if (validateInputs(email, password)) {
            // Log in the user with the provided email and password using Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Check if the login operation was successful
                            if (task.isSuccessful()) {
                                // Login successful, get the currently signed-in user
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Close the login activity
                                }
                            } else {
                                // Login failed, show the error message
                                Toast.makeText(Login.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    // This method validates that the email and password inputs are not empty
    private boolean validateInputs(String email, String password) {
        // Regular expression for valid email pattern
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        // Check if email matches the valid pattern
        if (!email.matches(emailPattern)) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password is empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Both email and password are valid
        return true;
    }

}