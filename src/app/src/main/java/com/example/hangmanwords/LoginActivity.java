package com.example.hangmanwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button btnRegister;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnToRegisterForm);

        btnLogin.setOnClickListener(onClickListener);
        btnRegister.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            Intent intent = null;

            switch(view.getId()){
                case R.id.btnLogin:

                    if(etUsername.getText().length() == 0 ||
                       etPassword.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please, enter your username and password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Check if exists in db
                    if(true){
                        intent = new Intent(LoginActivity.this, GameActivity.class);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please, register!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.btnToRegisterForm:
                    intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    break;
            }

            startActivity(intent);
        }
    };
}