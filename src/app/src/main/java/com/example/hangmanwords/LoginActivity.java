package com.example.hangmanwords;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button btnRegister;
    Button btnLogin;
    SQLiteHelper sqLiteHelper;

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

        sqLiteHelper = new SQLiteHelper(LoginActivity.this);
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

                    boolean isExisting = sqLiteHelper.userExists(etUsername.getText().toString());

                    if(isExisting){

                        User loginCredentials = new User();
                        loginCredentials.setUsername(etUsername.getText().toString());

                        User userInputModel = sqLiteHelper.getSingleUserInfo(loginCredentials);

                        //qwe
                        //123
                        boolean isVerified = comparePasswordHash(userInputModel.getHashedPassword(), etPassword.getText().toString());

                        if (isVerified){
                            intent = new Intent(LoginActivity.this, GameActivity.class);
                            intent.putExtra("user",userInputModel);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Check your username or password!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please, register!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    }

                    break;
                case R.id.btnToRegisterForm:
                    intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    break;
            }

            startActivity(intent);
        }
    };

    private boolean comparePasswordHash(String hashedPassword, String password) {
        String currentPasswordHash = PasswordHasher.get_SHA_512_SecurePassword(password);

        return hashedPassword.equals(currentPasswordHash);
    };


}