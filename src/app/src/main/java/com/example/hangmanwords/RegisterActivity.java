package com.example.hangmanwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button register;
    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsernameRegister);
        etPassword = findViewById(R.id.etPasswordRegister);
        register = findViewById(R.id.btnCreateRegistration);

        register.setOnClickListener(registerOnClickListener);

        sqLiteHelper = new SQLiteHelper(RegisterActivity.this);
    }

    View.OnClickListener registerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(etUsername.getText().length() == 0 ||
                    etPassword.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please, enter your username and password!", Toast.LENGTH_SHORT).show();
                return;
            }

            User userInputModel = new User();
            userInputModel.username = etUsername.getText().toString();
            userInputModel.password = etPassword.getText().toString();

            boolean isRegistrationSuccessful = sqLiteHelper.registerUser(userInputModel);

            if (isRegistrationSuccessful){
                Intent intent = new Intent(RegisterActivity.this, GameActivity.class);

                User user = sqLiteHelper.getSingleUserInfo(userInputModel);

                intent.putExtra("user", user);
                startActivity(intent);
                return;
            }

            Toast.makeText(getApplicationContext(), "You are already registered, please log in!", Toast.LENGTH_SHORT).show();
        }
    };

}