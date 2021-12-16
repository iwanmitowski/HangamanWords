package com.example.hangmanwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ScoresActivity extends AppCompatActivity {

    List<String> topThreeUsers;
    User currentUser;
    Button btnRetry;
    ListView lvUsers;
    ArrayAdapter arrayAdapter;
    GifImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        btnRetry = findViewById(R.id.btnRetry);
        lvUsers = findViewById(R.id.lvUsers);

        currentUser = getIntent().getParcelableExtra("user");

        SQLiteHelper sqLiteHelper = new SQLiteHelper(ScoresActivity.this);

        topThreeUsers = sqLiteHelper.topThreeUsers();
        arrayAdapter = new ArrayAdapter<String>(ScoresActivity.this, android.R.layout.simple_list_item_1, topThreeUsers);
        lvUsers.setAdapter(arrayAdapter);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoresActivity.this, GameActivity.class);
                intent.putExtra("user", currentUser);

                startActivity(intent);
            }
        });
    }
}