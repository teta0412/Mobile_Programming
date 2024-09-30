package com.map.nguyenanhduc.bt1.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.map.nguyenanhduc.bt1.R;
import com.map.nguyenanhduc.bt1.model.User;

public class LoginAct extends AppCompatActivity {
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize UI elements
        txtUsername = findViewById(R.id.editTextUsername);
        txtPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);

        // Set a click listener for the login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve entered username and password
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                User authen_user = new User("B21DCVT134","12345678");
                // Implement authentication logic here
                if (username.equals(authen_user.getUsername()) && password.equals(authen_user.getPassword())) {
                    // Successful login
                    Toast.makeText(LoginAct.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginAct.this, HomeAct.class);
                    intent.putExtra("user",authen_user);
                    startActivity(intent);
                } else {
                    // Failed login
                    Toast.makeText(LoginAct.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
