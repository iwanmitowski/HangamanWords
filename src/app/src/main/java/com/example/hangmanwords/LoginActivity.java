package com.example.hangmanwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import at.favre.lib.crypto.bcrypt.BCrypt;

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
                        loginCredentials.username = etUsername.getText().toString();

                        User userInputModel = sqLiteHelper.getSingleUserInfo(loginCredentials);

                        //qwe
                        //123
                        boolean isVerified = comparePasswordHash(userInputModel.hashedPassword, etPassword.getText().toString());

                        if (isVerified){
                            intent = new Intent(LoginActivity.this, GameActivity.class);
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
        String currentPasswordHash = get_SHA_512_SecurePassword(password);

        return hashedPassword.equals(currentPasswordHash);
    };

    public String get_SHA_512_SecurePassword(String passwordToHash){
        String salt = "";
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8)); //The empty string is the salt!
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }
}