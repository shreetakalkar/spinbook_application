package com.example.spinbook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {
    EditText name, surname, username, password, con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.firstname);
        surname = findViewById(R.id.surname);
        username = findViewById(R.id.usern);
        password = findViewById(R.id.pass);
        con = findViewById(R.id.confrim);
    }

    public void addrecord(View view) {
        String enteredName = name.getText().toString().trim();
        String enteredSurname = surname.getText().toString().trim();
        String enteredUsername = username.getText().toString().trim();
        String enteredPassword = password.getText().toString();
        String enteredConfirmPassword = con.getText().toString();

        // Check if any field is empty
        if (TextUtils.isEmpty(enteredName) || TextUtils.isEmpty(enteredSurname) ||
                TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty(enteredPassword) || TextUtils.isEmpty(enteredConfirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if passwords match
        if (!TextUtils.equals(enteredPassword, enteredConfirmPassword)) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_LONG).show();
            return; // Don't proceed with insertion
        }

        // All fields are filled, proceed with registration
        dbhandler db = new dbhandler(this);
        String res;
        res = db.addrecord(enteredName, enteredSurname, enteredUsername, enteredPassword);
        Toast.makeText(this, res, Toast.LENGTH_LONG).show();

        // Clear the input fields
        name.setText("");
        surname.setText("");
        username.setText("");
        password.setText("");
        con.setText("");

        // Navigate to the login screen
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }
}
