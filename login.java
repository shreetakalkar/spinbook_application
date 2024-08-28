// login.java
package com.example.spinbook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.button);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();

                if (areFieldsEmpty()) {
                    Toast.makeText(login.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    dbhandler db = new dbhandler(login.this);
                    boolean loginSuccessful = db.checkRecord(enteredUsername, enteredPassword);

                    // Modify your loginButton onClickListener
                    if (loginSuccessful) {
                        // Successful login, perform necessary actions (e.g., navigate to the main activity)
                        Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login.this, maindashboard.class);
                        intent.putExtra("USERNAME", enteredUsername);
                        startActivity(intent);
                        finish();
                    } else {
                        // Failed login
                        Toast.makeText(login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
    }

    private boolean areFieldsEmpty() {
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);

        return TextUtils.isEmpty(usernameEditText.getText()) || TextUtils.isEmpty(passwordEditText.getText());
    }
}
